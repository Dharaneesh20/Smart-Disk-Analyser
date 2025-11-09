# Building Smart Disk Analyzer for Windows

This guide explains how to build and package the Windows desktop application.

## 🎯 Quick Start

### Option 1: Test in Development Mode (Recommended First)

```powershell
.\test-electron.ps1
```

This will:
- Build backend and frontend (if needed)
- Install Electron dependencies
- Launch the app in development mode
- No installer needed - just test the app!

### Option 2: Build Windows Installer (Simplified)

```powershell
.\build-windows-simple.ps1
```

This builds a Windows x64 installer **without** bundled JRE (requires Java 17+ on target system).

**Build Output:**
- `electron\dist\Smart Disk Analyzer Setup 1.0.0.exe` - Full installer (~60 MB)
- `electron\dist\Smart Disk Analyzer 1.0.0.exe` - Portable executable (~60 MB)

### Option 3: Build with Embedded JRE (Full Distribution)

1. **Download JRE 17+**:
   - Go to: https://adoptium.net/temurin/releases/?version=17
   - Download "JRE" (not JDK) for Windows x64
   - Extract the zip file

2. **Place JRE**:
   ```
   Smart Disk Analyzer/
   └── electron/
       └── jre/           <-- Create this folder
           ├── bin/
           ├── lib/
           └── ...
   ```

3. **Run Build**:
   ```powershell
   .\build-windows-simple.ps1
   ```

**Build Output:**
- Installer will be ~180-200 MB (includes JRE)
- Works on systems without Java installed

## 📋 Prerequisites

### Required
- **Java 17+**: Already installed ✅ (You have Java 21.0.7)
- **Maven 3.6+**: Already installed ✅ (You have Maven 3.9.11)
- **Node.js 16+**: Already installed ✅
- **npm**: Already installed ✅

### Optional (for full distribution)
- **JRE 17+**: For bundling with installer

## 🔧 Build Process Explained

### What Gets Built

1. **Backend JAR** (`backend/target/*.jar`)
   - Spring Boot executable JAR with all dependencies
   - Size: ~50 MB
   - Embedded backend.jar

2. **Frontend Build** (`frontend/build/`)
   - Optimized React production build
   - Static HTML/CSS/JS files
   - Size: ~2 MB

3. **Electron App** (`electron/dist/`)
   - Windows executable wrapper
   - Embedded Electron runtime
   - Packaged with NSIS installer
   - Size: ~60 MB (without JRE) or ~180 MB (with JRE)

### Build Steps

#### Backend Build
```powershell
cd backend
mvn clean package -DskipTests
```

**Output**: `backend/target/disk-cleanup-partition-assistant-1.0.0.jar`

#### Frontend Build
```powershell
cd frontend
npm run build
```

**Output**: `frontend/build/` directory with optimized assets

#### Electron Package
```powershell
cd electron
npm install              # Install dependencies
npm run build:win        # Build Windows x64 installer
```

**Output**: 
- `electron/dist/win-unpacked/` - Unpacked application
- `electron/dist/*.exe` - Installers and portable executables

## 🚀 Distribution

### Files to Distribute

**Option A: Installer (Recommended)**
```
Smart Disk Analyzer Setup 1.0.0.exe
```
Users run this to install the application.

**Option B: Portable**
```
Smart Disk Analyzer 1.0.0.exe
```
No installation needed - just run the exe.

### System Requirements for Users

**Without Bundled JRE:**
- Windows 11 or Windows 10
- Java 17 or higher installed
- Administrator privileges

**With Bundled JRE:**
- Windows 11 or Windows 10
- Administrator privileges
- No Java installation needed!

## 🐛 Troubleshooting

### Build Fails at Backend

**Error**: Maven build failed

**Solution**:
```powershell
cd backend
mvn clean install -U  # Force update dependencies
```

### Build Fails at Frontend

**Error**: npm build failed

**Solution**:
```powershell
cd frontend
rm -rf node_modules package-lock.json
npm install
npm run build
```

### Build Fails at Electron

**Error**: electron-builder download interrupted

**Solution**:
- Check internet connection
- Wait for download to complete (108 MB)
- Don't interrupt the build process
- Run again: `.\build-windows-simple.ps1`

### Electron App Won't Start

**Error**: Backend failed to start

**Solution**:
- Ensure port 8080 is not in use
- Check Java is installed (if JRE not bundled)
- Run as Administrator

## 📦 Build Artifacts

After a successful build:

```
electron/
├── dist/
│   ├── win-unpacked/          # Unpacked application
│   ├── Smart Disk Analyzer Setup 1.0.0.exe  # NSIS installer
│   ├── Smart Disk Analyzer 1.0.0.exe        # Portable exe
│   └── builder-effective-config.yaml        # Build config
└── node_modules/              # Dependencies
```

## 🔐 Code Signing (Optional)

For production releases, you should code sign the executable:

1. **Obtain Code Signing Certificate**
   - From DigiCert, Sectigo, or similar CA
   - Costs ~$100-300/year

2. **Configure electron-builder**
   ```json
   "win": {
     "certificateFile": "path/to/cert.pfx",
     "certificatePassword": "password"
   }
   ```

3. **Build with Signing**
   ```powershell
   $env:CSC_LINK="path/to/cert.pfx"
   $env:CSC_KEY_PASSWORD="password"
   .\build-windows-simple.ps1
   ```

## 📊 Build Size Breakdown

| Component | Size | Notes |
|-----------|------|-------|
| Electron Runtime | ~110 MB | Chromium + Node.js |
| Backend JAR | ~50 MB | Spring Boot + dependencies |
| Frontend Build | ~2 MB | React app (optimized) |
| JRE (optional) | ~120 MB | Java Runtime |
| **Total (no JRE)** | **~165 MB** | Requires Java on system |
| **Total (with JRE)** | **~285 MB** | Standalone |

After compression (NSIS):
- Without JRE: ~60-70 MB
- With JRE: ~180-200 MB

## ⚡ Performance Tips

### Faster Builds

1. **Skip Tests**:
   ```powershell
   mvn package -DskipTests
   ```

2. **Use Maven Daemon**:
   ```powershell
   mvnnd clean package  # If installed
   ```

3. **Parallel Builds**:
   ```powershell
   mvn -T 1C package  # 1 thread per CPU core
   ```

### Smaller Output

1. **Exclude JRE** (if acceptable):
   - Remove `jre` from `extraResources` in `package.json`
   - Document Java requirement

2. **Use UPX Compression** (optional):
   ```json
   "win": {
     "target": {
       "target": "nsis",
       "arch": ["x64"]
     }
   }
   ```

## 📝 Notes

- **First build** takes longer (downloads Electron, ~110 MB)
- **Subsequent builds** are faster (uses cached Electron)
- **Internet required** for first build only
- **Build time**: ~5-10 minutes depending on hardware
- **Disk space**: Need ~500 MB free for build artifacts

## 🎯 Next Steps

After successful build:

1. **Test the installer** on a clean Windows machine
2. **Create GitHub Release** and upload installers
3. **Write release notes** documenting features
4. **Update README** with download links
5. **Announce release** to community

## 📞 Need Help?

- Check [GitHub Issues](https://github.com/Dharaneesh20/Smart-Disk-Analyser/issues)
- Read [CONTRIBUTING.md](CONTRIBUTING.md)
- Join [Discussions](https://github.com/Dharaneesh20/Smart-Disk-Analyser/discussions)

---

**Happy Building! 🚀**
