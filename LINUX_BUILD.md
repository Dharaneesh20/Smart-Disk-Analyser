# Building Smart Disk Analyzer for Linux

This guide explains how to build `.deb` installer packages for Ubuntu/Debian-based Linux systems.

## 📋 Prerequisites

### Required Software

1. **Node.js** (v16 or higher)
   ```bash
   curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
   sudo apt-get install -y nodejs
   ```

2. **Maven** (for Java backend)
   ```bash
   sudo apt-get install maven
   ```

3. **Java Development Kit** (JDK 17 or higher)
   ```bash
   sudo apt-get install openjdk-17-jdk
   ```

4. **Build tools**
   ```bash
   sudo apt-get install dpkg-dev dpkg build-essential
   ```

5. **Optional: ImageMagick** (for icon generation)
   ```bash
   sudo apt-get install imagemagick
   ```

## 🔨 Build Methods

### Method 1: Quick Build (Recommended)

Uses `electron-builder` to create packages automatically.

```bash
# Make script executable
chmod +x build-linux-simple.sh

# Run the build
./build-linux-simple.sh
```

**Output:**
- `electron/dist/smart-disk-analyzer_1.0.0_amd64.deb` - Debian package
- `electron/dist/Smart-Disk-Analyzer-1.0.0.AppImage` - Universal Linux package

### Method 2: Custom Build

Creates a fully customized `.deb` package with custom scripts and directory structure.

```bash
# Make script executable
chmod +x build-linux-deb.sh

# Run the build
./build-linux-deb.sh
```

**Output:**
- `smart-disk-analyzer_1.0.0_amd64.deb` - Custom Debian package

This method provides:
- Custom installation scripts
- Desktop integration
- Icon installation
- Service management
- Better control over dependencies

## 📦 Installation

### Install the .deb Package

```bash
# Install the package
sudo dpkg -i smart-disk-analyzer_1.0.0_amd64.deb

# Fix dependencies if needed
sudo apt-get install -f
```

### Verify Installation

```bash
# Check if installed
dpkg -l | grep smart-disk-analyzer

# Run the application
smart-disk-analyzer
```

### Launch from GUI

After installation, you can find "Smart Disk Analyzer" in your application menu under:
- **System** → **Smart Disk Analyzer**
- Or search for "Smart Disk Analyzer" in your launcher

## 🗑️ Uninstallation

```bash
# Remove the application
sudo apt-get remove smart-disk-analyzer

# Remove configuration files too
sudo apt-get purge smart-disk-analyzer
```

## 🔧 Build Options

### Build Different Formats

You can build multiple Linux package formats:

```bash
cd electron

# Build .deb only
npm run build:linux

# Build .deb, .rpm, and AppImage
npm run build:linux-all
```

### Customize Package

Edit `electron/package.json` to customize:

```json
{
  "build": {
    "linux": {
      "target": ["deb", "AppImage", "rpm"],
      "category": "System;Utility",
      "maintainer": "your.email@example.com"
    },
    "deb": {
      "depends": [
        "openjdk-17-jre"
      ]
    }
  }
}
```

## 📋 System Requirements

### Minimum Requirements
- **OS:** Ubuntu 20.04 LTS or Debian 11+
- **CPU:** Dual-core processor
- **RAM:** 2 GB
- **Disk:** 200 MB free space
- **Java:** OpenJDK 17 or higher

### Recommended Requirements
- **OS:** Ubuntu 22.04 LTS or later
- **CPU:** Quad-core processor
- **RAM:** 4 GB
- **Disk:** 500 MB free space
- **Java:** OpenJDK 17 or higher

## 🐛 Troubleshooting

### Build Fails with "mvn: command not found"

```bash
sudo apt-get install maven
```

### Missing Dependencies Error

```bash
# Install all required dependencies
sudo apt-get install -y nodejs npm maven openjdk-17-jdk dpkg-dev
```

### Electron Build Fails

```bash
# Clear npm cache and reinstall
cd electron
rm -rf node_modules package-lock.json
npm cache clean --force
npm install
```

### Backend JAR Not Found

Make sure the backend builds successfully:

```bash
cd backend
mvn clean package
ls -lh target/*.jar
```

### Application Won't Start

Check if Java is installed:

```bash
java -version
```

Check if backend JAR exists:

```bash
ls -lh /opt/smart-disk-analyzer/backend.jar
```

View logs:

```bash
journalctl --user -xeu smart-disk-analyzer
```

### Permission Denied

Some disk operations require root privileges:

```bash
# Run with sudo (not recommended for regular use)
sudo smart-disk-analyzer

# Or add your user to disk group
sudo usermod -a -G disk $USER
# Logout and login again
```

## 🔐 Running with Elevated Privileges

For disk partition management operations, the application needs root access:

### Option 1: PolicyKit (Recommended)

Create a PolicyKit rule:

```bash
sudo nano /etc/polkit-1/localauthority/50-local.d/smart-disk-analyzer.pkla
```

Add:

```ini
[Smart Disk Analyzer]
Identity=unix-user:*
Action=org.freedesktop.udisks2.*
ResultAny=auth_admin
ResultInactive=auth_admin
ResultActive=auth_admin
```

### Option 2: Sudo Wrapper

Create a wrapper script:

```bash
sudo nano /usr/local/bin/smart-disk-analyzer-admin
```

Add:

```bash
#!/bin/bash
pkexec /usr/bin/smart-disk-analyzer
```

Make executable:

```bash
sudo chmod +x /usr/local/bin/smart-disk-analyzer-admin
```

## 📚 Additional Information

### Package Contents

The `.deb` package includes:

- `/opt/smart-disk-analyzer/` - Application files
  - `backend.jar` - Spring Boot backend
  - `frontend/` - React UI files
  - `main.js` - Electron main process
  - `node_modules/` - Electron dependencies
  - `start.sh` - Application launcher

- `/usr/bin/smart-disk-analyzer` - Command-line launcher
- `/usr/share/applications/smart-disk-analyzer.desktop` - Desktop entry
- `/usr/share/icons/hicolor/256x256/apps/smart-disk-analyzer.png` - Application icon
- `/usr/share/doc/smart-disk-analyzer/` - Documentation

### Development Build

For development without creating a package:

```bash
# Build backend
cd backend && mvn clean package && cd ..

# Build frontend
cd frontend && npm install && npm run build && cd ..

# Run Electron in dev mode
cd electron && npm install && npm start
```

## 🤝 Contributing

If you improve the build scripts or fix issues:

1. Test on multiple Linux distributions
2. Update this documentation
3. Submit a pull request

## 📄 License

MIT License - See LICENSE file for details

## 🔗 Links

- **Website:** https://dharaneesh20.github.io/Smart-Disk-Analyser/
- **GitHub:** https://github.com/Dharaneesh20/Smart-Disk-Analyser
- **Issues:** https://github.com/Dharaneesh20/Smart-Disk-Analyser/issues
