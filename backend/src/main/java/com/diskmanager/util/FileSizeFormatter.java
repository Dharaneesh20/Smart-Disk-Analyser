package com.diskmanager.util;

/**
 * Utility class for file size formatting
 */
public class FileSizeFormatter {
    
    private static final long KB = 1024;
    private static final long MB = KB * 1024;
    private static final long GB = MB * 1024;
    private static final long TB = GB * 1024;
    
    /**
     * Format bytes to human-readable format
     * @param bytes size in bytes
     * @return formatted string
     */
    public static String formatSize(long bytes) {
        if (bytes < 0) {
            return "Invalid";
        }
        
        if (bytes < KB) {
            return bytes + " B";
        } else if (bytes < MB) {
            return String.format("%.2f KB", (double) bytes / KB);
        } else if (bytes < GB) {
            return String.format("%.2f MB", (double) bytes / MB);
        } else if (bytes < TB) {
            return String.format("%.2f GB", (double) bytes / GB);
        } else {
            return String.format("%.2f TB", (double) bytes / TB);
        }
    }
    
    /**
     * Parse size string to bytes
     * @param sizeStr size string (e.g., "100MB", "1.5GB")
     * @return size in bytes
     */
    public static long parseSize(String sizeStr) {
        if (sizeStr == null || sizeStr.isEmpty()) {
            return 0;
        }
        
        sizeStr = sizeStr.toUpperCase().trim();
        
        if (sizeStr.endsWith("TB")) {
            return (long) (Double.parseDouble(sizeStr.replace("TB", "")) * TB);
        } else if (sizeStr.endsWith("GB")) {
            return (long) (Double.parseDouble(sizeStr.replace("GB", "")) * GB);
        } else if (sizeStr.endsWith("MB")) {
            return (long) (Double.parseDouble(sizeStr.replace("MB", "")) * MB);
        } else if (sizeStr.endsWith("KB")) {
            return (long) (Double.parseDouble(sizeStr.replace("KB", "")) * KB);
        } else if (sizeStr.endsWith("B")) {
            return Long.parseLong(sizeStr.replace("B", ""));
        }
        
        return Long.parseLong(sizeStr);
    }
}
