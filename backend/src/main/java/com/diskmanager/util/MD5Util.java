package com.diskmanager.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Utility class for MD5 hash calculation
 */
public class MD5Util {
    
    /**
     * Calculate MD5 hash of a file
     * @param filePath path to the file
     * @return MD5 hash as hex string
     * @throws IOException if file cannot be read
     */
    public static String calculateMD5(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            return DigestUtils.md5Hex(fis);
        }
    }
    
    /**
     * Calculate MD5 hash with size limit for performance
     * For very large files, only hash first and last chunks
     * @param filePath path to the file
     * @param fileSize size of the file
     * @return MD5 hash as hex string
     */
    public static String calculateMD5Smart(String filePath, long fileSize) throws IOException {
        // For files larger than 1GB, use quick hash
        if (fileSize > 1024 * 1024 * 1024) {
            return calculateQuickHash(filePath, fileSize);
        }
        return calculateMD5(filePath);
    }
    
    /**
     * Quick hash for large files - hash first 1MB + size + last 1MB
     */
    private static String calculateQuickHash(String filePath, long fileSize) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[1024 * 1024]; // 1MB
            int read = fis.read(buffer);
            String hash = DigestUtils.md5Hex(buffer, 0, read);
            
            // Skip to last 1MB
            long skipBytes = fileSize - (1024 * 1024);
            if (skipBytes > 1024 * 1024) {
                fis.skip(skipBytes - (1024 * 1024));
                read = fis.read(buffer);
                hash += DigestUtils.md5Hex(buffer, 0, read);
            }
            
            return hash + "_" + fileSize;
        }
    }
}
