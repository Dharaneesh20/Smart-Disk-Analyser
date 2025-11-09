const { app, BrowserWindow, ipcMain, dialog, shell } = require('electron');
const path = require('path');
const { spawn } = require('child_process');
const find = require('find-process');
const fs = require('fs');

let mainWindow;
let backendProcess;
const BACKEND_PORT = 8080;
const FRONTEND_PORT = 3000;

// Check if running in development or production
const isDev = process.env.NODE_ENV === 'development' || !app.isPackaged;

// Log startup info
console.log('='.repeat(50));
console.log('Smart Disk Analyzer Starting...');
console.log('='.repeat(50));
console.log('isDev:', isDev);
console.log('app.isPackaged:', app.isPackaged);
console.log('__dirname:', __dirname);
console.log('process.resourcesPath:', process.resourcesPath);
console.log('app.getAppPath():', app.getAppPath());
console.log('='.repeat(50));

// Write startup log to file for debugging
try {
  const logPath = path.join(app.getPath('userData'), 'startup.log');
  const logContent = `
Smart Disk Analyzer Startup Log
================================
Time: ${new Date().toISOString()}
isDev: ${isDev}
app.isPackaged: ${app.isPackaged}
__dirname: ${__dirname}
process.resourcesPath: ${process.resourcesPath}
app.getAppPath(): ${app.getAppPath()}
app.getPath('userData'): ${app.getPath('userData')}
================================
`;
  fs.writeFileSync(logPath, logContent);
  console.log('Startup log written to:', logPath);
} catch (err) {
  console.error('Failed to write startup log:', err);
}

function createWindow() {
  // Create the browser window
  mainWindow = new BrowserWindow({
    width: 1400,
    height: 900,
    minWidth: 1200,
    minHeight: 700,
    icon: path.join(__dirname, 'assets/icon.ico'),
    webPreferences: {
      nodeIntegration: false,
      contextIsolation: true,
      preload: path.join(__dirname, 'preload.js')
    },
    title: 'Smart Disk Analyzer - Free Open Source Partition Manager',
    backgroundColor: '#1e1e1e',
    show: false // Don't show until ready
  });

  // Remove menu bar
  mainWindow.setMenuBarVisibility(false);

  // Show window when ready
  mainWindow.once('ready-to-show', () => {
    mainWindow.show();
  });

  // Load the app
  if (isDev) {
    // In development, use the React dev server
    mainWindow.loadURL('http://localhost:3000');
    mainWindow.webContents.openDevTools();
  } else {
    // In production, serve from built files
    const frontendPath = path.join(process.resourcesPath, 'frontend', 'index.html');
    console.log('Loading frontend from:', frontendPath);
    
    mainWindow.loadFile(frontendPath).catch(err => {
      console.error('Failed to load frontend:', err);
      dialog.showErrorBox('Failed to Load', 'Could not load the application interface.\n\nPath: ' + frontendPath);
    });
    
    // Enable dev tools temporarily for debugging
    // TODO: Remove this line before final release
    mainWindow.webContents.openDevTools();
  }

  // Log any page load errors
  mainWindow.webContents.on('did-fail-load', (event, errorCode, errorDescription, validatedURL) => {
    console.error('Page failed to load:', errorCode, errorDescription, validatedURL);
  });

  mainWindow.webContents.on('console-message', (event, level, message, line, sourceId) => {
    console.log(`[Frontend Console - ${level}]:`, message, 'at', sourceId, 'line', line);
  });

  // Handle external links
  mainWindow.webContents.setWindowOpenHandler(({ url }) => {
    shell.openExternal(url);
    return { action: 'deny' };
  });

  // Clean up on close
  mainWindow.on('closed', () => {
    mainWindow = null;
  });
}

async function startBackend() {
  console.log('Starting backend server...');
  const fs = require('fs');

  try {
    // Check if backend is already running
    const existingProcess = await find('port', BACKEND_PORT);
    if (existingProcess.length > 0) {
      console.log('Backend already running on port', BACKEND_PORT);
      return;
    }

    // Determine Java path with multiple fallbacks
    let javaPath = null;
    let javaPathsToTry = [];

    if (isDev) {
      javaPathsToTry = ['java'];
    } else {
      // In production, try multiple locations
      const bundledJavaPath = path.join(process.resourcesPath, 'jre', 'bin', 'java.exe');
      javaPathsToTry = [
        bundledJavaPath,
        'java', // System PATH
        path.join(process.env.JAVA_HOME || '', 'bin', 'java.exe'),
        'C:\\Program Files\\Eclipse Adoptium\\jdk-21.0.7.6-hotspot\\bin\\java.exe',
        'C:\\Program Files\\Eclipse Adoptium\\jdk-17.0.12.7-hotspot\\bin\\java.exe',
        'C:\\Program Files\\Java\\jdk-21\\bin\\java.exe',
        'C:\\Program Files\\Java\\jdk-17\\bin\\java.exe'
      ].filter(p => p); // Remove empty strings
    }

    // Try to find a working Java installation
    for (const testPath of javaPathsToTry) {
      try {
        if (testPath !== 'java' && !fs.existsSync(testPath)) {
          continue;
        }
        
        // Test if Java works
        const testResult = await new Promise((resolve) => {
          const testProcess = spawn(testPath, ['-version'], { windowsHide: true });
          testProcess.on('close', (code) => {
            resolve(code === 0);
          });
          testProcess.on('error', () => {
            resolve(false);
          });
        });

        if (testResult) {
          javaPath = testPath;
          console.log('Found working Java at:', javaPath);
          break;
        }
      } catch (err) {
        continue;
      }
    }

    if (!javaPath) {
      throw new Error('JAVA_NOT_FOUND');
    }

    // Determine JAR path
    const jarPath = isDev
      ? path.join(__dirname, '..', 'backend', 'target', 'disk-cleanup-partition-assistant-1.0.0.jar')
      : path.join(process.resourcesPath, 'backend.jar');

    // Verify JAR exists
    if (!fs.existsSync(jarPath)) {
      throw new Error(`Backend JAR not found at: ${jarPath}`);
    }

    console.log('Java path:', javaPath);
    console.log('JAR path:', jarPath);

    // Start the Spring Boot backend with proper error handling
    backendProcess = spawn(javaPath, [
      '-jar',
      jarPath,
      '--server.port=' + BACKEND_PORT
    ], {
      windowsHide: true,
      stdio: ['ignore', 'pipe', 'pipe']
    });

    // Handle spawn errors immediately
    backendProcess.on('error', (error) => {
      console.error('Failed to start backend process:', error);
      throw error;
    });

    backendProcess.stdout.on('data', (data) => {
      console.log('[Backend]', data.toString());
    });

    backendProcess.stderr.on('data', (data) => {
      const message = data.toString();
      // Don't log Spring Boot's normal startup messages as errors
      if (message.includes('ERROR') || message.includes('Exception')) {
        console.error('[Backend Error]', message);
      } else {
        console.log('[Backend]', message);
      }
    });

    backendProcess.on('close', (code) => {
      console.log('Backend process exited with code', code);
      if (code !== 0 && code !== null) {
        console.error('Backend crashed with exit code:', code);
      }
    });

    // Wait for backend to be ready
    await waitForBackend();
    console.log('Backend server ready!');

  } catch (error) {
    console.error('Failed to start backend:', error);
    
    // Provide detailed error messages
    if (error.message === 'JAVA_NOT_FOUND' || error.code === 'ENOENT') {
      dialog.showErrorBox(
        'Java Not Found',
        'Smart Disk Analyzer requires Java 17 or higher.\n\n' +
        'Java was not found on your system.\n\n' +
        'Please install Java from:\n' +
        'https://adoptium.net/temurin/releases/?version=17\n\n' +
        'Make sure to:\n' +
        '1. Download and install Java 17 or higher\n' +
        '2. Restart your computer\n' +
        '3. Run this application again'
      );
    } else if (error.message.includes('JAR not found')) {
      dialog.showErrorBox(
        'Installation Error',
        'Application files are missing or corrupted.\n\n' +
        'Error: ' + error.message + '\n\n' +
        'Please reinstall Smart Disk Analyzer.'
      );
    } else if (error.message.includes('failed to start within timeout')) {
      dialog.showErrorBox(
        'Backend Startup Timeout',
        'The backend server is taking too long to start.\n\n' +
        'This may be caused by:\n' +
        '• Antivirus blocking the application\n' +
        '• Firewall blocking port 8080\n' +
        '• Another application using port 8080\n\n' +
        'Please check these issues and try again.'
      );
    } else {
      dialog.showErrorBox(
        'Backend Start Failed',
        'Failed to start the backend server.\n\n' +
        'Error: ' + error.message + '\n\n' +
        'Please ensure:\n' +
        '• Java 17 or higher is installed\n' +
        '• Port 8080 is not in use\n' +
        '• Antivirus is not blocking the application'
      );
    }
    app.quit();
  }
}

async function waitForBackend(maxRetries = 30) {
  for (let i = 0; i < maxRetries; i++) {
    try {
      const http = require('http');
      await new Promise((resolve, reject) => {
        const req = http.get(`http://localhost:${BACKEND_PORT}/api/disk/health`, (res) => {
          if (res.statusCode === 200) {
            resolve();
          } else {
            reject(new Error('Backend not ready'));
          }
        });
        req.on('error', reject);
        req.setTimeout(1000);
      });
      return; // Backend is ready
    } catch (error) {
      console.log(`Waiting for backend... (${i + 1}/${maxRetries})`);
      await new Promise(resolve => setTimeout(resolve, 1000));
    }
  }
  throw new Error('Backend failed to start within timeout');
}

function stopBackend() {
  if (backendProcess) {
    console.log('Stopping backend server...');
    backendProcess.kill();
    backendProcess = null;
  }
}

// App lifecycle
app.whenReady().then(async () => {
  /* Temporarily disable admin check for testing
  // Check if running as administrator on Windows
  if (process.platform === 'win32') {
    const isAdmin = await checkAdminPrivileges();
    if (!isAdmin) {
      const result = dialog.showMessageBoxSync({
        type: 'warning',
        title: 'Administrator Privileges Required',
        message: 'Smart Disk Analyzer requires administrator privileges for partition operations.',
        detail: 'Please restart the application as administrator.\n\nNote: You can still use disk scanning features without admin privileges.',
        buttons: ['Continue Anyway', 'Exit'],
        defaultId: 1,
        cancelId: 1
      });

      if (result === 1) {
        app.quit();
        return;
      }
    }
  }
  */

  await startBackend();
  createWindow();

  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
      createWindow();
    }
  });
});

app.on('window-all-closed', () => {
  stopBackend();
  if (process.platform !== 'darwin') {
    app.quit();
  }
});

app.on('before-quit', () => {
  stopBackend();
});

// IPC Handlers
ipcMain.handle('get-app-version', () => {
  return app.getVersion();
});

ipcMain.handle('check-admin', async () => {
  return await checkAdminPrivileges();
});

ipcMain.handle('open-external', async (event, url) => {
  await shell.openExternal(url);
});

ipcMain.handle('show-dialog', async (event, options) => {
  return await dialog.showMessageBox(mainWindow, options);
});

async function checkAdminPrivileges() {
  if (process.platform !== 'win32') {
    return false;
  }

  try {
    const { spawn } = require('child_process');
    return new Promise((resolve) => {
      const child = spawn('net', ['session'], { windowsHide: true });
      child.on('exit', (code) => {
        resolve(code === 0);
      });
      child.on('error', () => {
        resolve(false);
      });
    });
  } catch (error) {
    return false;
  }
}

// Handle uncaught exceptions
process.on('uncaughtException', (error) => {
  console.error('Uncaught exception:', error);
  // Don't quit on errors, just log them
  if (mainWindow) {
    mainWindow.webContents.send('error', error.message);
  }
});

process.on('unhandledRejection', (reason, promise) => {
  console.error('Unhandled Rejection at:', promise, 'reason:', reason);
  // Don't quit on promise rejections
});
