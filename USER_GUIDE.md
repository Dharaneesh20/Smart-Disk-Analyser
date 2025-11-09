# Smart Disk Analyzer - User Guide

Welcome to Smart Disk Analyzer! This guide will help you get the most out of your free and open-source disk management tool.

## 📖 Table of Contents

1. [Getting Started](#getting-started)
2. [Dashboard Overview](#dashboard-overview)
3. [Disk Scanner](#disk-scanner)
4. [Duplicate File Finder](#duplicate-file-finder)
5. [Large File Finder](#large-file-finder)
6. [Partition Manager](#partition-manager)
7. [Advanced Partition Operations](#advanced-partition-operations)
8. [Safety Guidelines](#safety-guidelines)
9. [Troubleshooting](#troubleshooting)
10. [FAQ](#faq)

## 🚀 Getting Started

### System Requirements

- **Operating System**: Windows 11 or Windows 10
- **RAM**: 2 GB minimum (4 GB recommended)
- **Disk Space**: 200 MB for installation
- **Privileges**: Administrator access required for partition operations
- **Java**: Bundled with installer (no separate installation needed)

### Installation

1. **Download** the installer from [GitHub Releases](https://github.com/Dharaneesh20/Smart-Disk-Analyser/releases)
2. **Run** the `.exe` installer
3. Follow the installation wizard
4. **Launch** from the Start Menu or desktop shortcut

### First Launch

When you first open Smart Disk Analyzer:

1. The application will check for **administrator privileges**
2. You'll see the **Dashboard** with an overview of your disks
3. All features are immediately available - no registration needed!

> ⚠️ **Important**: Many features require administrator privileges. If prompted, click "Yes" to allow elevated access.

## 📊 Dashboard Overview

The Dashboard is your central hub showing:

### Real-Time Statistics
- **Total Files Scanned**: Number of files analyzed
- **Total Size**: Combined size of all scanned files
- **Duplicate Groups**: Number of duplicate file clusters found
- **Potential Space Savings**: Disk space that could be recovered

### Visual Charts
- **Pie Chart**: File type distribution (Images, Videos, Documents, etc.)
- **Bar Chart**: Top file types by size
- **Health Status**: Color-coded partition health indicators

### Quick Actions
- **Refresh**: Update statistics
- **Scan New Directory**: Start a new scan
- **View Details**: Jump to specific analysis tools

## 🔍 Disk Scanner

The Disk Scanner recursively analyzes directories to understand your storage usage.

### How to Scan

1. Navigate to **Disk Scanner** from the sidebar
2. Click **"Select Directory"** or enter a path manually
3. Configure scan settings:
   - **Max Depth**: How deep to scan subdirectories (1-10)
   - **File Extensions**: Filter specific file types (e.g., `.jpg, .pdf`)
   - **Include Hidden Files**: Check to scan hidden files
4. Click **"Scan"**
5. Wait for results (progress bar shows status)

### Understanding Results

**File Tree View:**
- Hierarchical display of all files and folders
- Size shown for each item
- Click folders to expand/collapse

**Statistics:**
- Total files found
- Total size
- Average file size
- Largest file

**File Type Breakdown:**
- Visual chart showing storage by type
- Categories: Images, Videos, Documents, Archives, Code, Other

### Tips for Effective Scanning

✅ **Do:**
- Start with specific folders (Documents, Downloads)
- Use file extension filters to narrow results
- Scan when you're not actively using the computer

❌ **Don't:**
- Scan entire C:\ drive without filters (too slow)
- Scan system folders (Windows, Program Files)
- Interrupt scans in progress

## 🔄 Duplicate File Finder

Find and remove duplicate files wasting disk space.

### How It Works

Smart Disk Analyzer uses **MD5 hashing** to identify exact duplicates:
1. Calculates unique fingerprint for each file
2. Groups files with identical fingerprints
3. Shows potential space savings

### Finding Duplicates

1. Go to **Duplicate Finder**
2. Click **"Scan for Duplicates"**
3. Select directory to scan
4. Wait for analysis (may take time for large directories)

### Review Results

**Duplicate Groups:**
Each group shows:
- Number of duplicate copies
- File size
- Total wasted space
- Locations of all copies

**Actions:**
- **Preview**: View file details
- **Keep One**: Select which copy to keep
- **Delete Others**: Remove duplicate copies
- **Mark All in Group**: Batch operations

### Best Practices

✅ **Safe to Delete:**
- Downloads folder duplicates
- Backup copies in multiple locations
- Temporary file duplicates
- Duplicate photos/videos

⚠️ **Be Careful With:**
- System files
- Program files
- Configuration files
- Files from different applications

❌ **Never Delete:**
- Files in Windows or Program Files folders
- Anything you're unsure about
- Files currently in use

### Recovery

Deleted files go to **Recycle Bin** - you can restore them if needed!

## 📏 Large File Finder

Identify files consuming the most disk space.

### Finding Large Files

1. Navigate to **Large Files**
2. Set **minimum file size** (default: 10 MB)
3. Choose **directory to scan**
4. Click **"Find Large Files"**

### Results Display

**Table View:**
| File Name | Size | Type | Location | Actions |
|-----------|------|------|----------|---------|
| movie.mp4 | 1.5 GB | Video | C:\Users\... | Open, Delete |

**Sort Options:**
- By size (largest first)
- By type
- By date modified
- By location

### What to Do With Large Files

**Options:**
1. **Delete**: Permanently remove (use carefully!)
2. **Move**: Relocate to external drive
3. **Compress**: Archive with tools like 7-Zip
4. **Keep**: If file is important

**Common Large Files:**
- Video files (`.mp4`, `.avi`, `.mov`)
- ISO images (`.iso`)
- Virtual machine files (`.vhd`, `.vmdk`)
- Large archives (`.zip`, `.rar`)
- Database backups (`.bak`, `.sql`)

## 💾 Partition Manager

View and manage all disk partitions on your system.

### Viewing Partitions

The **Partition Manager** shows:
- All detected partitions (C:\, D:\, etc.)
- Total size and used space
- File system type (NTFS, FAT32, exFAT)
- Health status
- Drive letters

### Partition Information

For each partition:
- **Total Space**: Total capacity
- **Used Space**: Currently occupied
- **Free Space**: Available space
- **Usage %**: Percentage full
- **File System**: NTFS, FAT32, or exFAT
- **Status**: Healthy, Warning, or Critical

### Health Status Colors

- 🟢 **Green (Healthy)**: < 80% full
- 🟡 **Yellow (Warning)**: 80-90% full
- 🔴 **Red (Critical)**: > 90% full

### Quick Actions

- **View Details**: See detailed partition info
- **Open in Explorer**: Browse partition contents
- **Check Health**: Run health diagnostics
- **Advanced Operations**: Access partition tools (requires admin)

## 🛠️ Advanced Partition Operations

> ⚠️ **WARNING**: These operations can cause data loss! Always backup first!

### Accessing Advanced Features

1. Go to **Partition Manager**
2. Click **"Advanced Operations"**
3. Confirm **administrator privileges**
4. Select the operation you need

### Create New Partition

**Use Case**: Add a new partition to organize data

**Steps:**
1. Click **"Create Partition"**
2. Select **disk** (Disk 0, Disk 1, etc.)
3. Choose **partition size** in GB
4. Select **file system**:
   - **NTFS**: For Windows (recommended)
   - **FAT32**: For compatibility (max 4GB files)
   - **exFAT**: For large files on USB drives
5. Click **"Create"**
6. Wait for completion

**Requirements:**
- Unallocated space on disk
- Administrator privileges
- No programs using the disk

### Delete Partition

**Use Case**: Remove unwanted partitions

**Steps:**
1. Click **"Delete Partition"**
2. Select **partition** to remove (e.g., "D:")
3. **Confirm deletion** (data will be lost!)
4. Click **"Delete"**

⚠️ **WARNING**: 
- All data on the partition will be permanently lost!
- Backup important files first!
- Cannot delete system partition (C:\)

### Format Partition

**Use Case**: Erase all data and reset partition

**Steps:**
1. Click **"Format Partition"**
2. Select **partition** to format
3. Choose **file system** (NTFS, FAT32, exFAT)
4. Enter **volume label** (optional)
5. Click **"Format"**

⚠️ **WARNING**: Formatting erases all data!

### Resize Partition

#### Shrink Partition

**Use Case**: Make partition smaller to free up space

**Steps:**
1. Click **"Shrink Partition"**
2. Select **partition** to shrink
3. Enter **amount to shrink** in GB
4. Click **"Shrink"**

**Limitations:**
- Cannot shrink beyond used space
- Some files cannot be moved
- May take several minutes

#### Extend Partition

**Use Case**: Make partition larger using adjacent free space

**Steps:**
1. Click **"Extend Partition"**
2. Select **partition** to extend
3. Enter **amount to extend** in GB
4. Click **"Extend"**

**Requirements:**
- Unallocated space adjacent to partition
- Space must be immediately after partition
- No data in unallocated region

### Change Drive Letter

**Use Case**: Reassign drive letter (C:, D:, E:, etc.)

**Steps:**
1. Click **"Change Drive Letter"**
2. Select **partition**
3. Choose **new drive letter**
4. Click **"Apply"**

**Notes:**
- Some programs may stop working if you change their drive
- Cannot change system drive (C:\)
- Reboot may be required

### Set Active Partition

**Use Case**: Mark partition as bootable

**Steps:**
1. Click **"Set Active"**
2. Select **partition** to mark active
3. Click **"Apply"**

⚠️ **DANGER**: Only do this if you understand boot processes!

### MBR/GPT Conversion

#### Convert to GPT

**Use Case**: Convert from MBR to GPT partition table

**Why GPT?**
- Supports disks > 2TB
- More reliable
- Required for UEFI boot
- Modern standard

**Steps:**
1. Click **"Convert to GPT"**
2. Select **disk** to convert
3. Confirm conversion
4. Click **"Convert"**

⚠️ **WARNING**: 
- Disk must be empty (backup and delete all partitions first)
- Cannot be undone without data loss
- Check BIOS compatibility

#### Convert to MBR

**Use Case**: Convert from GPT to MBR for older systems

**Steps:**
1. Click **"Convert to MBR"**
2. Select **disk** to convert
3. Confirm conversion
4. Click **"Convert"**

⚠️ **WARNING**: 
- Disk must be empty
- MBR limited to 2TB
- May not boot on UEFI-only systems

### Clean Disk

**Use Case**: Wipe entire disk and remove all partitions

**Steps:**
1. Click **"Clean Disk"**
2. Select **disk** to clean
3. **Triple-confirm** (this deletes everything!)
4. Click **"Clean"**

🔴 **EXTREME WARNING**: 
- **ALL DATA ON DISK WILL BE LOST!**
- **THIS CANNOT BE UNDONE!**
- Backup everything first!
- Wrong disk selection will destroy your system!

## 🛡️ Safety Guidelines

### Before Any Partition Operation

✅ **ALWAYS:**
1. **Backup your data** to external drive or cloud
2. **Close all programs** (especially those using the disk)
3. **Check disk health** (no existing errors)
4. **Ensure stable power** (UPS recommended)
5. **Read the warnings** and understand the risks
6. **Double-check** which disk/partition you selected

❌ **NEVER:**
1. Interrupt an operation in progress
2. Perform operations on a failing drive
3. Work on disks with important data without backup
4. Use the disk while operation is running
5. Force-shutdown during partition operations

### Data Safety Tips

1. **3-2-1 Backup Rule**:
   - 3 copies of important data
   - 2 different storage types
   - 1 offsite/cloud backup

2. **Test Your Backups**:
   - Verify files can be restored
   - Check backup integrity regularly

3. **Use Version Control**:
   - Keep multiple backup versions
   - Helps recover from ransomware

### When Things Go Wrong

**If operation fails:**
1. Don't panic!
2. Check error message
3. Reboot and check disk status
4. Run Windows Disk Check: `chkdsk /f`
5. See [Troubleshooting](#troubleshooting) section

**If disk becomes inaccessible:**
1. Don't attempt more operations!
2. Use professional data recovery tools
3. Consider professional recovery service
4. Restore from backup

## 🔧 Troubleshooting

### Common Issues

#### "Access Denied" Error

**Cause**: Insufficient privileges

**Solution**:
1. Right-click Smart Disk Analyzer
2. Select "Run as administrator"
3. Try operation again

#### "Disk in Use" Error

**Cause**: Programs are using the disk

**Solution**:
1. Close all programs
2. Close File Explorer windows
3. Check Task Manager for processes
4. Reboot and try again

#### Scan Takes Too Long

**Cause**: Too many files or slow disk

**Solution**:
1. Reduce max depth
2. Use file extension filters
3. Scan smaller directories
4. Close other programs

#### Partition Operations Fail

**Cause**: Various (disk issues, insufficient space, etc.)

**Solution**:
1. Check error message
2. Run `chkdsk /f` in Command Prompt (admin)
3. Ensure enough free space
4. Check disk is not failing
5. Reboot and try again

### Getting Help

If you can't resolve an issue:

1. **Check Documentation**: Read this guide thoroughly
2. **Search Issues**: [GitHub Issues](https://github.com/Dharaneesh20/Smart-Disk-Analyser/issues)
3. **Ask Community**: [GitHub Discussions](https://github.com/Dharaneesh20/Smart-Disk-Analyser/discussions)
4. **Report Bug**: Create a new issue with details

## ❓ FAQ

### General Questions

**Q: Is Smart Disk Analyzer really free?**  
A: Yes! 100% free, no hidden costs, no premium versions, no registration required.

**Q: Is it safe to use?**  
A: Yes, but partition operations always carry risk. **Always backup first!**

**Q: Can I use it commercially?**  
A: Yes! MIT license allows commercial use.

**Q: Does it collect my data?**  
A: No! Zero data collection, zero telemetry, 100% private.

### Feature Questions

**Q: Can it recover deleted files?**  
A: No, but files go to Recycle Bin and can be restored from there.

**Q: Can it defragment drives?**  
A: Not yet - this is planned for a future version.

**Q: Does it work on SSDs?**  
A: Yes! It works with both HDDs and SSDs.

**Q: Can it clone my entire system?**  
A: Disk cloning is in development (see [ROADMAP.md](ROADMAP.md)).

**Q: Can I resize my C:\ drive?**  
A: Yes, but be extremely careful! Backup first!

### Technical Questions

**Q: What file systems are supported?**  
A: NTFS, FAT32, and exFAT.

**Q: Does it work on Mac/Linux?**  
A: Not currently - Windows only for now.

**Q: Can I run it from a USB drive?**  
A: Not yet - portable version is planned.

**Q: Does it require internet?**  
A: No! Works completely offline.

**Q: What Windows versions are supported?**  
A: Windows 11 and Windows 10 (x86 and x64).

### Safety Questions

**Q: Can it damage my computer?**  
A: Partition operations carry inherent risks, but the tool includes safety checks.

**Q: What if I select the wrong disk?**  
A: Always double-check! Confirmation dialogs help prevent mistakes.

**Q: Can operations be undone?**  
A: No - partition operations are permanent! Backup first!

**Q: What if my computer crashes during operation?**  
A: This can cause data loss. Use a UPS and stable power.

### Comparison Questions

**Q: How is this better than AOMEI?**  
A: Free forever, open source, no registration, no ads, modern UI.

**Q: Is it as good as commercial tools?**  
A: Yes! We're implementing all major features. See [ROADMAP.md](ROADMAP.md).

**Q: Why choose this over Windows Disk Management?**  
A: More features, better UI, duplicate finder, large file finder, etc.

## 📚 Additional Resources

- **GitHub Repository**: [Smart-Disk-Analyser](https://github.com/Dharaneesh20/Smart-Disk-Analyser)
- **Report Issues**: [GitHub Issues](https://github.com/Dharaneesh20/Smart-Disk-Analyser/issues)
- **Feature Roadmap**: [ROADMAP.md](ROADMAP.md)
- **Contributing**: [CONTRIBUTING.md](CONTRIBUTING.md)
- **License**: [LICENSE](LICENSE) (MIT)

## 🎓 Tutorials

### Video Tutorials (Coming Soon)
- Getting Started with Smart Disk Analyzer
- How to Find and Remove Duplicate Files
- Safe Partition Resizing Guide
- MBR to GPT Conversion Tutorial

### Written Guides (Coming Soon)
- Complete Disk Cleanup Workflow
- Optimizing SSD Performance
- Safe System Migration

---

## 💡 Tips & Tricks

### Power User Tips

1. **Keyboard Shortcuts** (Coming Soon):
   - `Ctrl+R`: Refresh
   - `Ctrl+S`: Start scan
   - `Ctrl+D`: Duplicate finder
   - `F5`: Reload partitions

2. **Efficient Workflows**:
   - Scan Downloads folder monthly for duplicates
   - Check large files before major upgrades
   - Monitor partition health weekly

3. **Maintenance Schedule**:
   - Weekly: Check partition health
   - Monthly: Find and remove duplicates
   - Quarterly: Deep scan for large files
   - Yearly: Disk reorganization

### Performance Tips

- Close other programs during scans
- Scan during off-hours
- Use file filters to speed up searches
- Regular maintenance prevents slowdowns

---

<p align="center">
  <strong>Need more help? Visit our <a href="https://github.com/Dharaneesh20/Smart-Disk-Analyser/discussions">Community Discussions</a></strong>
</p>

<p align="center">
  Made with ❤️ by the Smart Disk Analyzer community
</p>

<p align="center">
  <strong>Free Forever | Open Source Always | Community Driven</strong>
</p>
