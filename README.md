# Smart Disk Analyzer

> **Free & Open Source Disk & Partition Manager for Windows & Linux**

A powerful, completely free alternative to AOMEI Partition Assistant and other commercial disk management tools. No registration, no hidden costs, no data collection - just a clean, open-source solution for managing your disks and partitions.

**🌐 Official Website**: [https://dharaneesh20.github.io/Smart-Disk-Analyser/](https://dharaneesh20.github.io/Smart-Disk-Analyser/)

![License](https://img.shields.io/badge/license-MIT-blue.svg)
![Platform](https://img.shields.io/badge/platform-Windows%2011%20%7C%2010%20%7C%20Linux-blue)
![Java](https://img.shields.io/badge/java-17%2B-orange)
![React](https://img.shields.io/badge/react-18-blue)

## 🔗 Quick Links

- 🌐 **[Official Website](https://dharaneesh20.github.io/Smart-Disk-Analyser/)** - Features, downloads, and documentation
- 📥 **[Download Latest Release](https://github.com/Dharaneesh20/Smart-Disk-Analyser/releases/latest)** - Get Windows or Linux version
- 🐛 **[Report Issues](https://github.com/Dharaneesh20/Smart-Disk-Analyser/issues)** - Bug reports and feature requests
- 💬 **[Discussions](https://github.com/Dharaneesh20/Smart-Disk-Analyser/discussions)** - Community discussions and support
- 📖 **[Documentation](https://dharaneesh20.github.io/Smart-Disk-Analyser/#docs)** - User guides and API reference

## 📥 Download

### Windows
- **[Windows Installer](https://github.com/Dharaneesh20/Smart-Disk-Analyser/releases/latest)** (.exe) - Recommended for most users
- **[Portable Version](https://github.com/Dharaneesh20/Smart-Disk-Analyser/releases/latest)** (.exe) - No installation required

### Linux (Ubuntu/Debian)
- **[Debian Package](https://github.com/Dharaneesh20/Smart-Disk-Analyser/releases/latest)** (.deb) - For Ubuntu, Debian, Linux Mint

**Visit our [website](https://dharaneesh20.github.io/Smart-Disk-Analyser/) for detailed download instructions and system requirements.**

## ✨ Features

### 🔍 Disk Analysis
- **Fast Directory Scanning**: Recursively scan directories with customizable depth
- **Duplicate File Detection**: Find duplicate files using MD5 hash comparison
- **Large File Finder**: Identify files consuming excessive disk space
- **File Type Statistics**: Visual breakdown of storage usage by file type
- **Disk Health Monitoring**: Real-time partition health status

### 💾 Partition Management
- **View All Partitions**: Complete overview of all disk partitions
- **Partition Health Check**: Monitor usage and health status
- **Create Partitions**: Create new partitions with custom size and file system
- **Delete Partitions**: Remove unwanted partitions safely
- **Format Partitions**: Format as NTFS, FAT32, or exFAT
- **Resize Partitions**: Shrink or extend partitions dynamically
- **Change Drive Letters**: Reassign drive letters easily
- **Set Active Partition**: Mark partitions as bootable

### 🛠️ Advanced Features (Comparable to AOMEI)
- **MBR/GPT Conversion**: Convert between partition table formats
- **Disk Cleaning**: Securely wipe disks and partitions
- **Administrator Detection**: Automatic privilege checking
- **Command-line Integration**: Uses native Windows diskpart
- **Safe Operations**: Built-in safety checks and validations

### 📊 Dashboard & Visualization
- **Real-time Statistics**: Live disk usage statistics
- **Interactive Charts**: Pie charts and bar graphs for data visualization
- **Health Status Indicators**: Color-coded warnings for low disk space
- **Multiple Views**: Dashboard, Scanner, Duplicates, Large Files, Partitions

## 🚀 Getting Started

### For End Users

#### Windows Installation

1. **Download** the latest installer from [Releases](https://github.com/Dharaneesh20/Smart-Disk-Analyser/releases/latest) or our [website](https://dharaneesh20.github.io/Smart-Disk-Analyser/)
   - **Full Installer** (recommended): Complete installation with Start Menu integration
   - **Portable Version**: Run directly without installation
2. **Run** the installer (administrator privileges required)
3. **Launch** Smart Disk Analyzer from Start Menu or desktop shortcut
4. **Start** managing your disks!

#### Linux Installation (Ubuntu/Debian)

**GUI Method:**
1. Download the `.deb` package from [Releases](https://github.com/Dharaneesh20/Smart-Disk-Analyser/releases/latest)
2. Double-click the downloaded file
3. Click "Install" in Software Center

**Terminal Method:**
```bash
# Download the .deb file, then:
sudo dpkg -i smart-disk-analyzer_1.0.0_amd64.deb
sudo apt-get install -f  # Fix dependencies if needed
```

> ⚠️ **Administrator Privileges Required**: Many partition operations require administrator/root access for safety.

> 📋 **System Requirements**: Visit our [website](https://dharaneesh20.github.io/Smart-Disk-Analyser/#download) for detailed system requirements.

### For Developers

#### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Node.js 16+ and npm
- Git

#### Quick Start

```bash
# Clone the repository
git clone https://github.com/Dharaneesh20/Smart-Disk-Analyser.git
cd Smart-Disk-Analyser

# Start backend and frontend
start.bat

# Or manually:
# Terminal 1 - Backend
cd backend
mvn spring-boot:run

# Terminal 2 - Frontend
cd frontend
npm install
npm start
```

Access the application at http://localhost:3000

#### Build Windows Application

```powershell
# Build complete Windows application with installer
.\build-windows.ps1
```

The installer will be created in `electron/dist/`

## 📖 Documentation

- **User Manual**: Complete guide for users (coming soon)
- **Developer Guide**: Setup and contribution guide (coming soon)
- **API Reference**: REST API documentation (coming soon)
- [Roadmap](ROADMAP.md) - Feature roadmap and development plans
- [Bug Fixes](BUGFIXES.md) - Known issues and fixes

## 🏗️ Project Structure

```
smart-disk-analyzer/
├── backend/                # Spring Boot REST API
│   ├── src/main/java/
│   │   └── com/diskmanager/
│   │       ├── controller/    # REST controllers
│   │       ├── service/       # Business logic
│   │       ├── model/         # JPA entities
│   │       ├── repository/    # Data access
│   │       └── util/          # Utilities
│   └── pom.xml
├── frontend/               # React web application
│   ├── src/
│   │   ├── components/    # Reusable components
│   │   ├── pages/         # Page components
│   │   ├── services/      # API integration
│   │   └── App.js
│   └── package.json
├── electron/              # Electron wrapper for desktop app
│   ├── main.js           # Main process
│   ├── preload.js        # Preload script
│   └── package.json
└── docs/                 # Documentation (coming soon)
```

## 🔧 Technology Stack

### Backend
- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: H2 (in-memory)
- **Build Tool**: Maven
- **Native Operations**: JNA for Windows API

### Frontend
- **Framework**: React 18
- **UI Library**: Material-UI (MUI)
- **Charts**: Recharts
- **HTTP Client**: Axios
- **Routing**: React Router

### Desktop
- **Framework**: Electron 28
- **Packaging**: electron-builder
- **Native APIs**: Windows diskpart integration

## 🤝 Contributing

We welcome contributions! This is a community-driven open-source project.

### How to Contribute

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add some AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

### Areas We Need Help

- 🐛 Bug fixes and testing
- 📝 Documentation improvements
- 🌍 Translations and internationalization
- ✨ New feature implementations
- 🎨 UI/UX improvements
- 🧪 Unit and integration tests

## 🆚 Why Choose Smart Disk Analyzer?

### vs. AOMEI Partition Assistant

| Feature | Smart Disk Analyzer | AOMEI Free | AOMEI Pro |
|---------|---------------------|------------|-----------|
| Price | **FREE** | FREE | $49.95+ |
| Open Source | ✅ | ❌ | ❌ |
| No Registration | ✅ | ❌ | ❌ |
| Partition Resize | ✅ | ✅ | ✅ |
| Create/Delete/Format | ✅ | ✅ | ✅ |
| MBR/GPT Convert | ✅ | ❌ | ✅ |
| Duplicate Finder | ✅ | ❌ | ❌ |
| Community Support | ✅ | ❌ | ✅ |
| Ad-Free | ✅ | ❌ | ✅ |

### Key Advantages

1. **💰 Completely Free**: No hidden costs, no premium versions
2. **🔓 Open Source**: Transparent code, community-driven
3. **🚫 No Bloatware**: Clean installation, no bundled software
4. **🛡️ Privacy**: No data collection or telemetry
5. **🌟 Modern UI**: Beautiful, intuitive interface
6. **📈 Active Development**: Regular updates and improvements

## ⚠️ Safety & Precautions

### Before Partition Operations:
- ✅ **Backup Important Data**: Always backup before major changes
- ✅ **Close Running Applications**: Ensure no programs are using the disk
- ✅ **Stable Power**: Use a UPS or ensure stable power supply
- ✅ **Check Disk Health**: Run disk health checks first
- ❌ **Don't Interrupt**: Never interrupt partition operations

### System Requirements

#### Windows
- **OS**: Windows 11, Windows 10 (x64)
- **RAM**: 4 GB minimum, 8 GB recommended
- **Disk**: 500 MB for installation
- **Privileges**: Administrator access required
- **Java**: Auto-detected or bundled with installer

#### Linux (Ubuntu/Debian)
- **OS**: Debian 11+, Ubuntu 20.04+, Linux Mint 20+
- **RAM**: 4 GB minimum, 8 GB recommended
- **Disk**: 500 MB for installation
- **Privileges**: Root/sudo access required
- **Java**: OpenJDK 17+ (auto-installed with package)
- **Desktop**: GNOME, KDE, XFCE, or other desktop environment

> 📋 For detailed requirements, visit our [website](https://dharaneesh20.github.io/Smart-Disk-Analyser/#download)

## 📊 Test Results

All API endpoints tested and verified ✅

| Endpoint | Status | Description |
|----------|--------|-------------|
| Health Check | ✅ PASS | API health monitoring |
| Get Statistics | ✅ PASS | Disk usage statistics |
| Scan Partitions | ✅ PASS | List all partitions |
| Partition Health | ✅ PASS | Health status check |
| List Partitions | ✅ PASS | Get partition details |
| Find Duplicates | ✅ PASS | Duplicate file detection |
| Get Large Files | ✅ PASS | Large file finder |
| Scan Directory | ✅ PASS | Recursive file scanning |
| Advanced Operations | ✅ PASS | Create/Delete/Format/Resize |

**10/10 Tests Passing** - Last tested: January 2025

## 🔌 API Endpoints

### Basic Operations
- `GET /api/disk/health` - Check API health
- `GET /api/disk/statistics` - Get disk usage stats
- `POST /api/disk/scan` - Scan directory
- `GET /api/disk/duplicates` - Find duplicate files
- `GET /api/disk/large-files` - Get large files

### Partition Operations
- `GET /api/partition/list` - List all partitions
- `GET /api/partition/health` - Check partition health

### Advanced Operations (Requires Admin)
- `GET /api/partition/advanced/check-admin` - Check admin privileges
- `GET /api/partition/advanced/disks` - List all disks
- `POST /api/partition/advanced/create` - Create partition
- `DELETE /api/partition/advanced/delete` - Delete partition
- `POST /api/partition/advanced/format` - Format partition
- `POST /api/partition/advanced/extend` - Extend partition
- `POST /api/partition/advanced/shrink` - Shrink partition
- `PUT /api/partition/advanced/change-letter` - Change drive letter
- `PUT /api/partition/advanced/set-active` - Set active partition
- `POST /api/partition/advanced/convert-gpt` - Convert to GPT
- `POST /api/partition/advanced/convert-mbr` - Convert to MBR
- `POST /api/partition/advanced/clean-disk` - Clean disk

## 📜 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

### What This Means:
- ✅ Free for personal and commercial use
- ✅ Modify and distribute freely
- ✅ Private use allowed
- ✅ No warranty provided
- ⚠️ Attribution required

## 🙏 Acknowledgments

- Inspired by AOMEI Partition Assistant and other disk management tools
- Built with modern open-source technologies
- Community-driven development and testing
- Special thanks to all contributors

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/Dharaneesh20/Smart-Disk-Analyser/issues)
- **Discussions**: [GitHub Discussions](https://github.com/Dharaneesh20/Smart-Disk-Analyser/discussions)
- **Repository**: [Smart-Disk-Analyser](https://github.com/Dharaneesh20/Smart-Disk-Analyser)

## 🗺️ Roadmap

See [ROADMAP.md](ROADMAP.md) for detailed development plans and upcoming features.

### Phase 1: Enhanced Partition Management ✅ (In Progress)
- ✅ Basic partition operations (create, delete, format)
- ✅ Resize operations (shrink, extend)
- ✅ MBR/GPT conversion
- ⬜ Partition merge and split
- ⬜ File system conversion

### Phase 2: Boot & Recovery 
- Bootable media creation
- Disk cloning and imaging
- OS migration tools
- Boot repair utilities

### Phase 3: Windows Integration
- System tray integration
- Context menu integration
- Windows notifications
- Scheduled tasks

### Phase 4: Additional Features
- S.M.A.R.T. monitoring
- SSD optimization
- Secure data wiping
- Multi-language support

## ⭐ Star History

If you find this project useful, please consider giving it a star! ⭐

---

<p align="center">
  Made with ❤️ by <a href="https://github.com/Dharaneesh20">Dharaneesh20</a> and contributors
</p>

<p align="center">
  <strong>Free Forever | Open Source Always | Community Driven</strong>
</p>
