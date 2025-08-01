package com.diskmanager.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for file type detection
 */
public class FileTypeDetector {
    
    private static final Map<String, String> EXTENSION_TO_TYPE = new HashMap<>();
    
    static {
        // Documents
        EXTENSION_TO_TYPE.put("pdf", "Document");
        EXTENSION_TO_TYPE.put("doc", "Document");
        EXTENSION_TO_TYPE.put("docx", "Document");
        EXTENSION_TO_TYPE.put("txt", "Document");
        EXTENSION_TO_TYPE.put("xls", "Document");
        EXTENSION_TO_TYPE.put("xlsx", "Document");
        EXTENSION_TO_TYPE.put("ppt", "Document");
        EXTENSION_TO_TYPE.put("pptx", "Document");
        EXTENSION_TO_TYPE.put("odt", "Document");
        EXTENSION_TO_TYPE.put("ods", "Document");
        
        // Images
        EXTENSION_TO_TYPE.put("jpg", "Image");
        EXTENSION_TO_TYPE.put("jpeg", "Image");
        EXTENSION_TO_TYPE.put("png", "Image");
        EXTENSION_TO_TYPE.put("gif", "Image");
        EXTENSION_TO_TYPE.put("bmp", "Image");
        EXTENSION_TO_TYPE.put("svg", "Image");
        EXTENSION_TO_TYPE.put("webp", "Image");
        EXTENSION_TO_TYPE.put("ico", "Image");
        EXTENSION_TO_TYPE.put("tiff", "Image");
        EXTENSION_TO_TYPE.put("raw", "Image");
        
        // Videos
        EXTENSION_TO_TYPE.put("mp4", "Video");
        EXTENSION_TO_TYPE.put("avi", "Video");
        EXTENSION_TO_TYPE.put("mkv", "Video");
        EXTENSION_TO_TYPE.put("mov", "Video");
        EXTENSION_TO_TYPE.put("wmv", "Video");
        EXTENSION_TO_TYPE.put("flv", "Video");
        EXTENSION_TO_TYPE.put("webm", "Video");
        EXTENSION_TO_TYPE.put("m4v", "Video");
        EXTENSION_TO_TYPE.put("mpg", "Video");
        EXTENSION_TO_TYPE.put("mpeg", "Video");
        
        // Audio
        EXTENSION_TO_TYPE.put("mp3", "Audio");
        EXTENSION_TO_TYPE.put("wav", "Audio");
        EXTENSION_TO_TYPE.put("flac", "Audio");
        EXTENSION_TO_TYPE.put("aac", "Audio");
        EXTENSION_TO_TYPE.put("ogg", "Audio");
        EXTENSION_TO_TYPE.put("wma", "Audio");
        EXTENSION_TO_TYPE.put("m4a", "Audio");
        EXTENSION_TO_TYPE.put("opus", "Audio");
        
        // Archives
        EXTENSION_TO_TYPE.put("zip", "Archive");
        EXTENSION_TO_TYPE.put("rar", "Archive");
        EXTENSION_TO_TYPE.put("7z", "Archive");
        EXTENSION_TO_TYPE.put("tar", "Archive");
        EXTENSION_TO_TYPE.put("gz", "Archive");
        EXTENSION_TO_TYPE.put("bz2", "Archive");
        EXTENSION_TO_TYPE.put("xz", "Archive");
        
        // Code
        EXTENSION_TO_TYPE.put("java", "Code");
        EXTENSION_TO_TYPE.put("py", "Code");
        EXTENSION_TO_TYPE.put("js", "Code");
        EXTENSION_TO_TYPE.put("ts", "Code");
        EXTENSION_TO_TYPE.put("jsx", "Code");
        EXTENSION_TO_TYPE.put("tsx", "Code");
        EXTENSION_TO_TYPE.put("c", "Code");
        EXTENSION_TO_TYPE.put("cpp", "Code");
        EXTENSION_TO_TYPE.put("h", "Code");
        EXTENSION_TO_TYPE.put("cs", "Code");
        EXTENSION_TO_TYPE.put("php", "Code");
        EXTENSION_TO_TYPE.put("rb", "Code");
        EXTENSION_TO_TYPE.put("go", "Code");
        EXTENSION_TO_TYPE.put("rs", "Code");
        EXTENSION_TO_TYPE.put("swift", "Code");
        EXTENSION_TO_TYPE.put("kt", "Code");
        EXTENSION_TO_TYPE.put("html", "Code");
        EXTENSION_TO_TYPE.put("css", "Code");
        EXTENSION_TO_TYPE.put("scss", "Code");
        EXTENSION_TO_TYPE.put("json", "Code");
        EXTENSION_TO_TYPE.put("xml", "Code");
        EXTENSION_TO_TYPE.put("yaml", "Code");
        EXTENSION_TO_TYPE.put("yml", "Code");
        
        // Executables
        EXTENSION_TO_TYPE.put("exe", "Executable");
        EXTENSION_TO_TYPE.put("dll", "Executable");
        EXTENSION_TO_TYPE.put("so", "Executable");
        EXTENSION_TO_TYPE.put("app", "Executable");
        EXTENSION_TO_TYPE.put("bat", "Executable");
        EXTENSION_TO_TYPE.put("sh", "Executable");
        EXTENSION_TO_TYPE.put("msi", "Executable");
    }
    
    /**
     * Detect file type based on extension
     * @param extension file extension (without dot)
     * @return file type category
     */
    public static String detectFileType(String extension) {
        if (extension == null || extension.isEmpty()) {
            return "Unknown";
        }
        
        String type = EXTENSION_TO_TYPE.get(extension.toLowerCase());
        return type != null ? type : "Other";
    }
    
    /**
     * Get file extension from filename
     * @param fileName name of the file
     * @return extension without dot
     */
    public static String getExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot > 0 && lastDot < fileName.length() - 1) {
            return fileName.substring(lastDot + 1).toLowerCase();
        }
        
        return "";
    }
}
