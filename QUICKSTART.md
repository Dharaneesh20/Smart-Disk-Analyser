# Quick Start Guide

Welcome to the **Smart Web Dashboard - Disk Cleanup and Partition Assistant**!

## 🚀 Quick Setup

### Prerequisites
- Java 17 or higher
- Node.js 16+ and npm
- Maven 3.6+

### Step 1: Start the Backend

Open a terminal and navigate to the backend directory:

```powershell
cd backend
```

Build and run the Spring Boot application:

```powershell
mvn clean install
mvn spring-boot:run
```

The backend API will start on `http://localhost:8080`

**Swagger UI:** http://localhost:8080/swagger-ui.html

### Step 2: Start the Frontend

Open a **new terminal** and navigate to the frontend directory:

```powershell
cd frontend
```

Install dependencies (first time only):

```powershell
npm install
```

Start the React development server:

```powershell
npm start
```

The frontend will start on `http://localhost:3000` and open automatically in your browser.

## 📖 Using the Application

### Dashboard
- View overall disk statistics
- See file type distribution charts
- Monitor partition health
- Check for wasted space from duplicates

### Disk Scanner
1. Enter a directory path (e.g., `C:\Users\YourName\Documents`)
2. Configure scan options (max depth, hidden files)
3. Click "Start Scan" to analyze files
4. View scanned files in the table

### Partition Manager
- Automatically scans all system partitions
- Shows usage percentage and health status
- Displays total, used, and free space
- Color-coded health indicators (HEALTHY, WARNING, CRITICAL)

### Duplicate Finder
1. Run a disk scan first
2. Click "Find Duplicates" to detect duplicate files
3. View duplicate groups with potential space savings
4. Review file paths and decide which copies to keep

### Large Files
1. Set a size threshold (default: 100MB)
2. Click "Find Large Files"
3. View all files exceeding the threshold
4. Identify candidates for cleanup or archival

## 🎯 Tips

- **Start with Partition Manager**: Get an overview of your disk health
- **Scan smaller directories first**: Test the scanner with a small folder
- **Review duplicates carefully**: Always keep at least one copy
- **Large files**: Video and archive files are usually the biggest space consumers

## 🔧 Troubleshooting

### Backend won't start
- Ensure Java 17+ is installed: `java -version`
- Check if port 8080 is available
- Review logs for any errors

### Frontend won't start
- Ensure Node.js is installed: `node -version`
- Clear npm cache: `npm cache clean --force`
- Delete `node_modules` and run `npm install` again

### Cannot scan directories
- Ensure you have read permissions for the directory
- Use absolute paths (e.g., `C:\Users\...` not `.\Documents`)
- Check that the path exists

## 📝 Notes

- **Partition operations** (resize/extend) are simulated for educational purposes
- Use system tools like **diskpart** (Windows) or **parted** (Linux) for actual partition operations
- The application uses MD5 hashing for duplicate detection
- Large files (>500MB) use smart hashing for performance

## 🎓 Learning Resources

This project demonstrates:
- Spring Boot REST APIs
- React with Material-UI
- File system operations in Java
- Data visualization with Recharts
- Multi-threaded processing
- MD5 hashing algorithms

Enjoy optimizing your disk space! 🎉
