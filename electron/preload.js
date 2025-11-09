const { contextBridge, ipcRenderer } = require('electron');

// Expose protected methods that allow the renderer process to use
// ipcRenderer without exposing the entire object
contextBridge.exposeInMainWorld('electronAPI', {
  // Get app version
  getAppVersion: () => ipcRenderer.invoke('get-app-version'),
  
  // Check admin privileges
  checkAdmin: () => ipcRenderer.invoke('check-admin'),
  
  // Open external link
  openExternal: (url) => ipcRenderer.invoke('open-external', url),
  
  // Show dialog
  showDialog: (options) => ipcRenderer.invoke('show-dialog', options),
  
  // Platform info
  platform: process.platform,
  isWindows: process.platform === 'win32',
  isMac: process.platform === 'darwin',
  isLinux: process.platform === 'linux'
});
