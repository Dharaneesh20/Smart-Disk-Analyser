const { app, BrowserWindow, ipcMain, dialog, shell } = require('electron');
const path = require('path');
const { spawn } = require('child_process');
const find = require('find-process');

let mainWindow;
let backendProcess;
const BACKEND_PORT = 8080;
const FRONTEND_PORT = 3000;

// Check if running in development or production
const isDev = process.env.NODE_ENV === 'development';

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
    mainWindow.loadFile(frontendPath);
  }

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

  try {
    // Check if backend is already running
    const existingProcess = await find('port', BACKEND_PORT);
    if (existingProcess.length > 0) {
      console.log('Backend already running on port', BACKEND_PORT);
      return;
    }

    // Determine Java path
    let javaPath;
    if (isDev) {
      javaPath = 'java'; // Use system Java in development
    } else {
      // Use bundled JRE in production
      javaPath = path.join(process.resourcesPath, 'jre', 'bin', 'java.exe');
    }

    // Determine JAR path
    const jarPath = isDev
      ? path.join(__dirname, '..', 'backend', 'target', 'disk-cleanup-partition-assistant-1.0.0.jar')
      : path.join(process.resourcesPath, 'backend.jar');

    console.log('Java path:', javaPath);
    console.log('JAR path:', jarPath);

    // Start the Spring Boot backend
    backendProcess = spawn(javaPath, [
      '-jar',
      jarPath,
      '--server.port=' + BACKEND_PORT
    ]);

    backendProcess.stdout.on('data', (data) => {
      console.log('[Backend]', data.toString());
    });

    backendProcess.stderr.on('data', (data) => {
      console.error('[Backend Error]', data.toString());
    });

    backendProcess.on('close', (code) => {
      console.log('Backend process exited with code', code);
    });

    // Wait for backend to be ready
    await waitForBackend();
    console.log('Backend server ready!');

  } catch (error) {
    console.error('Failed to start backend:', error);
    dialog.showErrorBox(
      'Backend Start Failed',
      'Failed to start the backend server. Please ensure Java is installed and try again.'
    );
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
  dialog.showErrorBox('Error', error.message);
});
