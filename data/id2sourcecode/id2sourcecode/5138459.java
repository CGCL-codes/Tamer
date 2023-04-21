    public static String copy(File srcFile, File destFile) throws IOException {
        destFile = getUniqueFile(destFile);
        FileUtils.copyFile(srcFile, destFile, true);
        return destFile.getAbsolutePath();
    }
