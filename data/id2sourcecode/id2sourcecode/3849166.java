    public static void copyFile(File srcFile, File destFile, boolean preserveFileDate) {
        try {
            FileUtils.copyFile(srcFile, destFile, preserveFileDate);
        } catch (Exception e) {
            throw new DCFileException(e);
        }
    }
