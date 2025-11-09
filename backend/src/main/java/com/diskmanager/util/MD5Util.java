package com.diskmanager.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

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
        final int CHUNK = 1024 * 1024; // 1MB
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
            byte[] buffer = new byte[CHUNK];

            // read first chunk
            int readFirst = raf.read(buffer);
            String firstHash = DigestUtils.md5Hex(Arrays.copyOf(buffer, Math.max(0, readFirst)));

            // read last chunk
            long lastPos = Math.max(0, fileSize - CHUNK);
            raf.seek(lastPos);
            int readLast = raf.read(buffer);
            String lastHash = DigestUtils.md5Hex(Arrays.copyOf(buffer, Math.max(0, readLast)));

            return firstHash + "_" + lastHash + "_" + fileSize;
        }
    }
}
