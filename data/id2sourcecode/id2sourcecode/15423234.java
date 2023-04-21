    public static void copy(File srcFile, File targetFile, boolean overwrite, FileFilter filter) throws FileNotFoundException, IOException {
        if (filter != null && !filter.accept(srcFile.getCanonicalFile())) return;
        if (!srcFile.exists()) throw new ConfigurationError("Source file not found: " + srcFile);
        if (!overwrite && targetFile.exists()) throw new ConfigurationError("Target file already exists: " + targetFile);
        if (srcFile.isFile()) copyFile(srcFile, targetFile); else copyDirectory(srcFile, targetFile, overwrite, filter);
    }
