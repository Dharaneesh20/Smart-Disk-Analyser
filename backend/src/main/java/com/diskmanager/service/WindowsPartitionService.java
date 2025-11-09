package com.diskmanager.service;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for advanced Windows partition operations using native APIs
 * Requires administrator privileges for most operations
 */
@Service
@Slf4j
public class WindowsPartitionService {
    
    /**
     * Execute diskpart command script
     */
    private String executeDiskpartScript(List<String> commands) throws IOException {
        log.info("Executing diskpart commands: {}", commands);
        
        // Create temp script file
        java.io.File scriptFile = java.io.File.createTempFile("diskpart_", ".txt");
        scriptFile.deleteOnExit();
        
        // Write commands to file
        java.nio.file.Files.write(scriptFile.toPath(), commands);
        
        // Execute diskpart with script
        ProcessBuilder pb = new ProcessBuilder("diskpart", "/s", scriptFile.getAbsolutePath());
        pb.redirectErrorStream(true);
        
        Process process = pb.start();
        StringBuilder output = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                log.debug("Diskpart output: {}", line);
            }
        }
        
        try {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("Diskpart exited with code: " + exitCode);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Diskpart execution interrupted", e);
        }
        
        return output.toString();
    }
    
    /**
     * List all physical disks
     */
    public List<DiskInfo> listDisks() throws IOException {
        List<String> commands = List.of(
            "list disk"
        );
        
        String output = executeDiskpartScript(commands);
        return parseDiskList(output);
    }
    
    /**
     * Parse disk list output
     */
    private List<DiskInfo> parseDiskList(String output) {
        List<DiskInfo> disks = new ArrayList<>();
        String[] lines = output.split("\n");
        boolean inDiskList = false;
        
        for (String line : lines) {
            if (line.contains("Disk ###")) {
                inDiskList = true;
                continue;
            }
            
            if (inDiskList && line.trim().startsWith("Disk ")) {
                try {
                    DiskInfo disk = parseDiskLine(line);
                    disks.add(disk);
                } catch (Exception e) {
                    log.warn("Failed to parse disk line: {}", line, e);
                }
            }
        }
        
        return disks;
    }
    
    /**
     * Parse single disk line
     */
    private DiskInfo parseDiskLine(String line) {
        // Example: "  Disk 0    Online       931 GB  1024 KB"
        String[] parts = line.trim().split("\\s+");
        
        DiskInfo disk = new DiskInfo();
        disk.setNumber(Integer.parseInt(parts[1]));
        disk.setStatus(parts[2]);
        disk.setSize(parts[3] + " " + parts[4]);
        
        return disk;
    }
    
    /**
     * Create new partition on specified disk
     */
    public String createPartition(int diskNumber, String sizeInMB, String fileSystem) throws IOException {
        log.info("Creating partition on disk {} with size {} MB and filesystem {}", 
                diskNumber, sizeInMB, fileSystem);
        
        List<String> commands = new ArrayList<>();
        commands.add("select disk " + diskNumber);
        commands.add("create partition primary size=" + sizeInMB);
        commands.add("format fs=" + fileSystem.toLowerCase() + " quick");
        commands.add("assign");
        
        return executeDiskpartScript(commands);
    }
    
    /**
     * Delete partition by volume letter
     */
    public String deletePartition(String volumeLetter) throws IOException {
        log.warn("Deleting partition: {}", volumeLetter);
        
        List<String> commands = List.of(
            "select volume " + volumeLetter,
            "delete partition"
        );
        
        return executeDiskpartScript(commands);
    }
    
    /**
     * Format partition
     */
    public String formatPartition(String volumeLetter, String fileSystem, String label) throws IOException {
        log.info("Formatting partition {} as {} with label {}", volumeLetter, fileSystem, label);
        
        List<String> commands = new ArrayList<>();
        commands.add("select volume " + volumeLetter);
        
        String formatCmd = "format fs=" + fileSystem.toLowerCase() + " quick";
        if (label != null && !label.isEmpty()) {
            formatCmd += " label=\"" + label + "\"";
        }
        commands.add(formatCmd);
        
        return executeDiskpartScript(commands);
    }
    
    /**
     * Extend partition (requires adjacent unallocated space)
     */
    public String extendPartition(String volumeLetter, String sizeInMB) throws IOException {
        log.info("Extending partition {} by {} MB", volumeLetter, sizeInMB);
        
        List<String> commands = List.of(
            "select volume " + volumeLetter,
            "extend size=" + sizeInMB
        );
        
        return executeDiskpartScript(commands);
    }
    
    /**
     * Shrink partition
     */
    public String shrinkPartition(String volumeLetter, String sizeInMB) throws IOException {
        log.info("Shrinking partition {} by {} MB", volumeLetter, sizeInMB);
        
        List<String> commands = List.of(
            "select volume " + volumeLetter,
            "shrink desired=" + sizeInMB
        );
        
        return executeDiskpartScript(commands);
    }
    
    /**
     * Change drive letter
     */
    public String changeDriveLetter(String currentLetter, String newLetter) throws IOException {
        log.info("Changing drive letter from {} to {}", currentLetter, newLetter);
        
        List<String> commands = List.of(
            "select volume " + currentLetter,
            "remove letter=" + currentLetter,
            "assign letter=" + newLetter
        );
        
        return executeDiskpartScript(commands);
    }
    
    /**
     * Set partition as active (bootable)
     */
    public String setActivePartition(int diskNumber, int partitionNumber) throws IOException {
        log.info("Setting partition {} on disk {} as active", partitionNumber, diskNumber);
        
        List<String> commands = List.of(
            "select disk " + diskNumber,
            "select partition " + partitionNumber,
            "active"
        );
        
        return executeDiskpartScript(commands);
    }
    
    /**
     * Convert disk to GPT (requires disk to be empty)
     */
    public String convertToGPT(int diskNumber) throws IOException {
        log.info("Converting disk {} to GPT", diskNumber);
        
        List<String> commands = List.of(
            "select disk " + diskNumber,
            "convert gpt"
        );
        
        return executeDiskpartScript(commands);
    }
    
    /**
     * Convert disk to MBR (requires disk to be empty)
     */
    public String convertToMBR(int diskNumber) throws IOException {
        log.info("Converting disk {} to MBR", diskNumber);
        
        List<String> commands = List.of(
            "select disk " + diskNumber,
            "convert mbr"
        );
        
        return executeDiskpartScript(commands);
    }
    
    /**
     * Clean disk (removes all partitions)
     */
    public String cleanDisk(int diskNumber) throws IOException {
        log.warn("Cleaning disk {} - ALL DATA WILL BE LOST", diskNumber);
        
        List<String> commands = List.of(
            "select disk " + diskNumber,
            "clean"
        );
        
        return executeDiskpartScript(commands);
    }
    
    /**
     * Check if running with administrator privileges
     */
    public boolean isAdministrator() {
        try {
            // Try to access a protected registry key
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                ProcessBuilder pb = new ProcessBuilder("net", "session");
                Process process = pb.start();
                int exitCode = process.waitFor();
                return exitCode == 0;
            }
        } catch (Exception e) {
            log.warn("Failed to check administrator privileges", e);
        }
        return false;
    }
    
    /**
     * DiskInfo inner class
     */
    public static class DiskInfo {
        private int number;
        private String status;
        private String size;
        private boolean isGPT;
        
        // Getters and setters
        public int getNumber() { return number; }
        public void setNumber(int number) { this.number = number; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getSize() { return size; }
        public void setSize(String size) { this.size = size; }
        
        public boolean isGPT() { return isGPT; }
        public void setGPT(boolean GPT) { isGPT = GPT; }
    }
}
