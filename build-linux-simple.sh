#!/bin/bash

################################################################################
# Smart Disk Analyzer - Simple Linux Build Script (using electron-builder)
# This script uses electron-builder to create .deb packages
################################################################################

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

echo -e "${BLUE}╔══════════════════════════════════════════════════╗${NC}"
echo -e "${BLUE}║  Smart Disk Analyzer - Linux Builder            ║${NC}"
echo -e "${BLUE}╚══════════════════════════════════════════════════╝${NC}"
echo ""

# Check prerequisites
echo -e "${YELLOW}→ Checking prerequisites...${NC}"

if ! command -v node &> /dev/null; then
    echo -e "${RED}✗ Node.js not installed${NC}"
    exit 1
fi

if ! command -v mvn &> /dev/null; then
    echo -e "${RED}✗ Maven not installed${NC}"
    exit 1
fi

echo -e "${GREEN}✓ Prerequisites OK${NC}"
echo ""

# Build backend
echo -e "${YELLOW}→ Building Spring Boot backend...${NC}"
cd backend
mvn clean package -DskipTests
cd ..
echo -e "${GREEN}✓ Backend built${NC}"
echo ""

# Build frontend
echo -e "${YELLOW}→ Building React frontend...${NC}"
cd frontend
[ ! -d "node_modules" ] && npm install
npm run build
cd ..
echo -e "${GREEN}✓ Frontend built${NC}"
echo ""

# Build Electron app with Linux target
echo -e "${YELLOW}→ Building Linux packages...${NC}"
cd electron
[ ! -d "node_modules" ] && npm install
npm run build:linux
cd ..
echo -e "${GREEN}✓ Linux packages built${NC}"
echo ""

# Show results
echo -e "${GREEN}╔══════════════════════════════════════════════════╗${NC}"
echo -e "${GREEN}║  Build Complete!                                 ║${NC}"
echo -e "${GREEN}╚══════════════════════════════════════════════════╝${NC}"
echo ""
echo -e "${BLUE}Output directory:${NC} electron/dist/"
echo ""
ls -lh electron/dist/*.deb 2>/dev/null || echo -e "${YELLOW}No .deb files found${NC}"
ls -lh electron/dist/*.AppImage 2>/dev/null || echo -e "${YELLOW}No AppImage files found${NC}"
echo ""
echo -e "${YELLOW}Installation:${NC}"
echo -e "  sudo dpkg -i electron/dist/*.deb"
echo -e "  sudo apt-get install -f"
echo ""
