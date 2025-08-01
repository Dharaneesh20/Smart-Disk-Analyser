# Smart Web Dashboard - Disk Cleanup and Partition Assistant

A comprehensive system utility application built with Spring Boot and React that helps users optimize their disk space and manage partitions effectively.

## 🌟 Features

### Disk Cleanup & Analysis
- **Smart Folder Scanning**: Recursively scans user directories to analyze disk usage
- **Duplicate File Detection**: Uses MD5 hashing to identify duplicate files across the system
- **Large File Visualization**: Interactive charts powered by Recharts to visualize storage usage
- **Smart Cleanup Suggestions**: AI-powered recommendations for safe file deletion and compression
- **File Category Analysis**: Breaks down storage by file types (documents, images, videos, etc.)

### Disk Partition Management
- **Partition Analyzer**: View detailed information about all disk partitions
- **Partition Resizer**: Safely resize partitions without data loss
- **Partition Extender**: Extend partition space by utilizing unallocated disk space
- **Health Monitoring**: Track partition health and performance metrics

## 🏗️ Architecture

### Backend (Spring Boot)
- **REST API** for all disk operations
- **Multi-threaded** file scanning for performance
- **MD5 hashing** algorithm for duplicate detection
- **JNA (Java Native Access)** for low-level disk operations
- **Scheduled tasks** for periodic cleanup monitoring

### Frontend (React)
- **Material-UI** for modern, responsive design
- **Recharts** for data visualization
- **Axios** for API communication
- **React Router** for navigation
- **Context API** for state management

## 📋 Prerequisites

- Java 17 or higher
- Node.js 16+ and npm
- Maven 3.6+
- Windows/Linux/macOS

## 🚀 Getting Started

### Backend Setup

1. Navigate to the backend directory:
```bash
cd backend
```

2. Build the application:
```bash
mvn clean install
```

3. Run the Spring Boot application:
```bash
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the development server:
```bash
npm start
```

The frontend will start on `http://localhost:3000`

## 🔧 API Endpoints

### Disk Analysis
- `GET /api/disk/scan` - Scan a directory for files
- `GET /api/disk/duplicates` - Find duplicate files
- `GET /api/disk/large-files` - Get list of large files
- `GET /api/disk/statistics` - Get disk usage statistics
- `POST /api/disk/cleanup` - Execute cleanup operations

### Partition Management
- `GET /api/partition/list` - List all partitions
- `GET /api/partition/{id}` - Get partition details
- `POST /api/partition/resize` - Resize a partition
- `POST /api/partition/extend` - Extend a partition
- `GET /api/partition/health` - Check partition health

## 📊 Screenshots

_(Screenshots will be added as the project develops)_

## 🛠️ Technology Stack

**Backend:**
- Spring Boot 3.2.0
- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- JNA (Java Native Access)

**Frontend:**
- React 18
- Material-UI
- Recharts
- Axios
- React Router

## 🤝 Contributing

This is a personal project rebuilt after a PC failure. Contributions are welcome!

## 📝 License

MIT License - feel free to use this project for learning and personal use.

## 🔄 Project History

This project was originally developed starting August 2025 but was lost due to a PC failure. This is a complete rebuild with improvements and additional features.

## 📧 Contact

For questions or suggestions, please open an issue in the repository.

---

**Built with ❤️ to help users optimize their systems**
