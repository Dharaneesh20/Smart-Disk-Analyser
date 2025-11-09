# BUILDING ON WINDOWS - PERMISSION ISSUE FIX

## ⚠️ Problem

You're encountering this error:
```
ERROR: Cannot create symbolic link : A required privilege is not held by the client
```

This happens because electron-builder tries to create symbolic links, which requires special privileges on Windows.

## ✅ Solutions (Choose ONE)

### Solution 1: Run PowerShell as Administrator (RECOMMENDED)

1. **Close current PowerShell**
2. **Right-click** on PowerShell icon
3. Select **"Run as Administrator"**
4. Navigate to project:
   ```powershell
   cd 'C:\Users\Dharaneesh\Desktop\Smart Web Dashboard Disk Cleanup and Disk Partition assistant'
   ```
5. Run build:
   ```powershell
   .\build-windows-simple.ps1
   ```

### Solution 2: Enable Developer Mode (Windows 10/11)

1. Open **Settings** (Win + I)
2. Go to **Privacy & Security** → **For developers**
3. Turn ON **Developer Mode**
4. Restart PowerShell
5. Run build again:
   ```powershell
   .\build-windows-simple.ps1
   ```

### Solution 3: Just Test the App (No Build Needed!)

Instead of creating an installer, just run the app in development mode:

```powershell
.\test-electron.ps1
```

This will:
- ✅ Start the app immediately
- ✅ No administrator rights needed
- ✅ No installer creation
- ✅ Perfect for testing

## 🎯 What to Do NOW

**For Testing Only:**
```powershell
.\test-electron.ps1
```

**For Creating Installer:**
1. Close PowerShell
2. Right-click PowerShell → Run as Administrator
3. Navigate to project folder
4. Run: `.\build-windows-simple.ps1`

## 📝 Why This Happens

- Windows restricts symbolic link creation by default
- electron-builder downloads code signing tools that use symlinks
- This is a security feature, not a bug

## 🔧 Technical Details

The error occurs when extracting `winCodeSign-2.6.0.7z` which contains macOS libraries with symbolic links. Even though we're building for Windows, electron-builder downloads this package.

**Workarounds tried:**
- ❌ Disabling code signing (`sign: null`) - Still downloads winCodeSign
- ❌ Setting `CSC_IDENTITY_AUTO_DISCOVERY=false` - Still needs to extract files
- ❌ Clearing cache - Cache gets corrupted again

**What works:**
- ✅ Administrator PowerShell - Has symlink privileges
- ✅ Developer Mode - Grants symlink privileges
- ✅ Directory-only build with admin rights

## 🚀 Quick Reference

### Test App (No Admin Needed)
```powershell
.\test-electron.ps1
```

### Build Portable App (Needs Admin)
```powershell
# Run PowerShell as Admin first!
.\quick-build.ps1
```

### Build Full Installer (Needs Admin)
```powershell
# Run PowerShell as Admin first!
.\build-windows-simple.ps1
```

## 📦 Alternative: Manual Packaging

If you can't get admin rights, you can manually package:

1. **Run test mode**: `.\test-electron.ps1`
2. **Verify app works**
3. **Copy these folders**:
   - `backend\target\disk-cleanup-partition-assistant-1.0.0.jar`
   - `frontend\build\`
   - `electron\` (main.js, preload.js, package.json)
4. **Share as ZIP** for others to run

## 💡 For Production Release

When ready for final release:
1. **Use CI/CD** (GitHub Actions, AppVeyor) - These have admin rights
2. **Code signing certificate** - For trusted installer
3. **Proper installer** - NSIS with all resources

---

**TL;DR:** Run PowerShell as Administrator and use `.\build-windows-simple.ps1`
