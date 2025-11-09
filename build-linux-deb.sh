#!/bin/bash

################################################################################
# Smart Disk Analyzer - Linux .deb Installer Build Script
# This script builds a Debian package for Ubuntu/Debian-based Linux systems
################################################################################

set -e  # Exit on any error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Application metadata
APP_NAME="smart-disk-analyzer"
APP_VERSION="1.0.0"
APP_DESCRIPTION="Free Open Source Disk & Partition Manager"
APP_MAINTAINER="Your Name <your.email@example.com>"
APP_HOMEPAGE="https://dharaneesh20.github.io/Smart-Disk-Analyser/"
ARCH="amd64"

# Directories
BUILD_DIR="build-linux"
DEB_DIR="$BUILD_DIR/debian"
APP_DIR="$DEB_DIR/opt/$APP_NAME"
BIN_DIR="$DEB_DIR/usr/bin"
DESKTOP_DIR="$DEB_DIR/usr/share/applications"
ICON_DIR="$DEB_DIR/usr/share/icons/hicolor/256x256/apps"
DOC_DIR="$DEB_DIR/usr/share/doc/$APP_NAME"

echo -e "${BLUE}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║  Smart Disk Analyzer - Debian Package Builder             ║${NC}"
echo -e "${BLUE}╚════════════════════════════════════════════════════════════╝${NC}"
echo ""

# Check prerequisites
echo -e "${YELLOW}→ Checking prerequisites...${NC}"

if ! command -v node &> /dev/null; then
    echo -e "${RED}✗ Node.js is not installed${NC}"
    exit 1
fi

if ! command -v npm &> /dev/null; then
    echo -e "${RED}✗ npm is not installed${NC}"
    exit 1
fi

if ! command -v mvn &> /dev/null; then
    echo -e "${RED}✗ Maven is not installed${NC}"
    echo -e "${YELLOW}  Install with: sudo apt-get install maven${NC}"
    exit 1
fi

if ! command -v dpkg-deb &> /dev/null; then
    echo -e "${RED}✗ dpkg-deb is not installed${NC}"
    echo -e "${YELLOW}  Install with: sudo apt-get install dpkg${NC}"
    exit 1
fi

echo -e "${GREEN}✓ All prerequisites met${NC}"
echo ""

# Clean previous builds
echo -e "${YELLOW}→ Cleaning previous builds...${NC}"
rm -rf "$BUILD_DIR"
mkdir -p "$APP_DIR"
mkdir -p "$BIN_DIR"
mkdir -p "$DESKTOP_DIR"
mkdir -p "$ICON_DIR"
mkdir -p "$DOC_DIR"
mkdir -p "$DEB_DIR/DEBIAN"
echo -e "${GREEN}✓ Build directory prepared${NC}"
echo ""

# Build Spring Boot Backend
echo -e "${YELLOW}→ Building Spring Boot backend...${NC}"
cd backend
mvn clean package -DskipTests
if [ ! -f "target/disk-cleanup-partition-assistant-$APP_VERSION.jar" ]; then
    echo -e "${RED}✗ Backend JAR not found${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Backend built successfully${NC}"
cd ..
echo ""

# Build React Frontend
echo -e "${YELLOW}→ Building React frontend...${NC}"
cd frontend
if [ ! -d "node_modules" ]; then
    npm install
fi
npm run build
if [ ! -d "build" ]; then
    echo -e "${RED}✗ Frontend build failed${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Frontend built successfully${NC}"
cd ..
echo ""

# Install Electron dependencies
echo -e "${YELLOW}→ Installing Electron dependencies...${NC}"
cd electron
if [ ! -d "node_modules" ]; then
    npm install
fi
cd ..
echo -e "${GREEN}✓ Electron dependencies installed${NC}"
echo ""

# Copy application files
echo -e "${YELLOW}→ Packaging application files...${NC}"

# Copy backend JAR
cp "backend/target/disk-cleanup-partition-assistant-$APP_VERSION.jar" "$APP_DIR/backend.jar"

# Copy frontend build
cp -r frontend/build "$APP_DIR/frontend"

# Copy electron files
cp -r electron/* "$APP_DIR/"
rm -rf "$APP_DIR/node_modules"
cp -r electron/node_modules "$APP_DIR/"

# Copy LICENSE and documentation
cp LICENSE "$DOC_DIR/copyright"
cp README.md "$DOC_DIR/"
cp USER_GUIDE.md "$DOC_DIR/" 2>/dev/null || true

echo -e "${GREEN}✓ Application files packaged${NC}"
echo ""

# Create launcher script
echo -e "${YELLOW}→ Creating launcher script...${NC}"
cat > "$APP_DIR/start.sh" << 'LAUNCHER_EOF'
#!/bin/bash
APP_DIR="/opt/smart-disk-analyzer"
cd "$APP_DIR"

# Start backend in background
if ! pgrep -f "backend.jar" > /dev/null; then
    java -jar backend.jar > /dev/null 2>&1 &
    BACKEND_PID=$!
    echo $BACKEND_PID > /tmp/smart-disk-analyzer-backend.pid
    
    # Wait for backend to start
    sleep 3
fi

# Start Electron UI
export ELECTRON_IS_DEV=0
./node_modules/.bin/electron .

# Cleanup on exit
if [ -f /tmp/smart-disk-analyzer-backend.pid ]; then
    kill $(cat /tmp/smart-disk-analyzer-backend.pid) 2>/dev/null || true
    rm /tmp/smart-disk-analyzer-backend.pid
fi
LAUNCHER_EOF

chmod +x "$APP_DIR/start.sh"

# Create symlink in /usr/bin
cat > "$BIN_DIR/$APP_NAME" << 'SYMLINK_EOF'
#!/bin/bash
/opt/smart-disk-analyzer/start.sh "$@"
SYMLINK_EOF

chmod +x "$BIN_DIR/$APP_NAME"

echo -e "${GREEN}✓ Launcher script created${NC}"
echo ""

# Create desktop entry
echo -e "${YELLOW}→ Creating desktop entry...${NC}"
cat > "$DESKTOP_DIR/$APP_NAME.desktop" << DESKTOP_EOF
[Desktop Entry]
Version=1.0
Type=Application
Name=Smart Disk Analyzer
Comment=$APP_DESCRIPTION
Exec=/usr/bin/$APP_NAME
Icon=$APP_NAME
Terminal=false
Categories=System;Utility;
Keywords=disk;partition;cleanup;storage;
StartupNotify=true
DESKTOP_EOF

echo -e "${GREEN}✓ Desktop entry created${NC}"
echo ""

# Create application icon (you should provide a proper icon)
echo -e "${YELLOW}→ Creating application icon...${NC}"
# If you have an icon file, copy it here
# cp path/to/icon.png "$ICON_DIR/$APP_NAME.png"

# Create a placeholder icon using ImageMagick (if available)
if command -v convert &> /dev/null; then
    convert -size 256x256 xc:blue -fill white -gravity center \
            -pointsize 48 -annotate +0+0 "SD" \
            "$ICON_DIR/$APP_NAME.png" 2>/dev/null || true
else
    echo -e "${YELLOW}  ! No icon created (ImageMagick not installed)${NC}"
    echo -e "${YELLOW}    Add your icon to: $ICON_DIR/$APP_NAME.png${NC}"
fi

echo ""

# Create DEBIAN control file
echo -e "${YELLOW}→ Creating package metadata...${NC}"
INSTALLED_SIZE=$(du -sk "$DEB_DIR" | cut -f1)

cat > "$DEB_DIR/DEBIAN/control" << CONTROL_EOF
Package: $APP_NAME
Version: $APP_VERSION
Section: utils
Priority: optional
Architecture: $ARCH
Depends: openjdk-17-jre | openjdk-18-jre | openjdk-19-jre, libgtk-3-0, libnotify4, libnss3, libxss1, libxtst6, xdg-utils, libatspi2.0-0, libuuid1, libsecret-1-0
Maintainer: $APP_MAINTAINER
Homepage: $APP_HOMEPAGE
Installed-Size: $INSTALLED_SIZE
Description: $APP_DESCRIPTION
 Smart Disk Analyzer is a free and open-source disk partition manager
 and cleanup tool for Linux systems. It provides:
 .
  - Disk space analysis and visualization
  - Partition management (resize, create, delete)
  - Duplicate file detection
  - Large file finder
  - Disk cleanup utilities
  - System health monitoring
 .
 This application combines a Spring Boot backend with an Electron-based
 user interface for a modern, user-friendly experience.
CONTROL_EOF

echo -e "${GREEN}✓ Package metadata created${NC}"
echo ""

# Create postinst script
echo -e "${YELLOW}→ Creating post-installation script...${NC}"
cat > "$DEB_DIR/DEBIAN/postinst" << 'POSTINST_EOF'
#!/bin/bash
set -e

# Update desktop database
if command -v update-desktop-database &> /dev/null; then
    update-desktop-database -q /usr/share/applications || true
fi

# Update icon cache
if command -v gtk-update-icon-cache &> /dev/null; then
    gtk-update-icon-cache -q /usr/share/icons/hicolor || true
fi

echo "Smart Disk Analyzer has been installed successfully!"
echo "You can launch it from your application menu or run: smart-disk-analyzer"

exit 0
POSTINST_EOF

chmod 755 "$DEB_DIR/DEBIAN/postinst"

# Create prerm script
cat > "$DEB_DIR/DEBIAN/prerm" << 'PRERM_EOF'
#!/bin/bash
set -e

# Stop any running instances
pkill -f "smart-disk-analyzer" 2>/dev/null || true
pkill -f "backend.jar" 2>/dev/null || true

exit 0
PRERM_EOF

chmod 755 "$DEB_DIR/DEBIAN/prerm"

# Create postrm script
cat > "$DEB_DIR/DEBIAN/postrm" << 'POSTRM_EOF'
#!/bin/bash
set -e

# Update desktop database
if command -v update-desktop-database &> /dev/null; then
    update-desktop-database -q /usr/share/applications || true
fi

# Update icon cache
if command -v gtk-update-icon-cache &> /dev/null; then
    gtk-update-icon-cache -q /usr/share/icons/hicolor || true
fi

exit 0
POSTRM_EOF

chmod 755 "$DEB_DIR/DEBIAN/postrm"

echo -e "${GREEN}✓ Installation scripts created${NC}"
echo ""

# Fix permissions
echo -e "${YELLOW}→ Setting correct permissions...${NC}"
find "$DEB_DIR" -type d -exec chmod 755 {} \;
find "$DEB_DIR/opt" -type f -exec chmod 644 {} \;
chmod 755 "$APP_DIR/start.sh"
chmod 755 "$BIN_DIR/$APP_NAME"
find "$APP_DIR/node_modules/.bin" -type f -exec chmod 755 {} \; 2>/dev/null || true
echo -e "${GREEN}✓ Permissions set${NC}"
echo ""

# Build the .deb package
echo -e "${YELLOW}→ Building .deb package...${NC}"
DEB_FILE="${APP_NAME}_${APP_VERSION}_${ARCH}.deb"
dpkg-deb --build "$DEB_DIR" "$BUILD_DIR/$DEB_FILE"

if [ ! -f "$BUILD_DIR/$DEB_FILE" ]; then
    echo -e "${RED}✗ Failed to create .deb package${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Package built successfully${NC}"
echo ""

# Move to root directory
mv "$BUILD_DIR/$DEB_FILE" "./$DEB_FILE"

# Get file size
FILE_SIZE=$(du -h "$DEB_FILE" | cut -f1)

# Verify package
echo -e "${YELLOW}→ Verifying package...${NC}"
dpkg-deb --info "$DEB_FILE"
echo ""

echo -e "${GREEN}╔════════════════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║  Build Complete!                                          ║${NC}"
echo -e "${GREEN}╚════════════════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${BLUE}Package created:${NC} $DEB_FILE"
echo -e "${BLUE}Size:${NC} $FILE_SIZE"
echo ""
echo -e "${YELLOW}Installation instructions:${NC}"
echo -e "  sudo dpkg -i $DEB_FILE"
echo -e "  sudo apt-get install -f  ${BLUE}# Fix dependencies if needed${NC}"
echo ""
echo -e "${YELLOW}Run application:${NC}"
echo -e "  smart-disk-analyzer"
echo ""
echo -e "${YELLOW}Uninstall:${NC}"
echo -e "  sudo apt-get remove $APP_NAME"
echo ""

# Optional: Test installation in docker
read -p "Do you want to test installation in a Docker container? (y/n) " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo -e "${YELLOW}→ Creating test Docker container...${NC}"
    cat > Dockerfile.test << DOCKERFILE_EOF
FROM ubuntu:22.04
RUN apt-get update && apt-get install -y sudo
COPY $DEB_FILE /tmp/
RUN dpkg -i /tmp/$DEB_FILE || apt-get install -f -y
CMD ["smart-disk-analyzer", "--version"]
DOCKERFILE_EOF
    
    docker build -f Dockerfile.test -t smart-disk-analyzer-test .
    docker run --rm smart-disk-analyzer-test
    rm Dockerfile.test
    echo -e "${GREEN}✓ Package tested successfully in Docker${NC}"
fi

echo -e "${GREEN}Done!${NC}"
