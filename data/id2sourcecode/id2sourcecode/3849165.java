    public static void copyFile(String srcFileName, String destFileName, boolean preserveFileDate) {
        if (srcFileName == null) {
            throw new DCFileException("Source must not be null");
        }
        if (destFileName == null) {
            throw new DCFileException("Destination must not be null");
        }
        File srcFile = new File(srcFileName);
        File destFile = new File(destFileName);
        if (!srcFile.exists()) {
            throw new DCFileException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new DCFileException("Source '" + srcFile + "' is a directory");
        }
        if (destFile.isDirectory()) {
            throw new DCFileException("Destination '" + destFile + "' is a directory");
        }
        try {
            FileUtils.copyFile(srcFile, destFile, preserveFileDate);
        } catch (Exception e) {
            throw new DCFileException(e);
        }
    }
