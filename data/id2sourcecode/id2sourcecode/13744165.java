    public static void renameFile(File srcFile, File destFile) throws IOException {
        FileUtils.copyFile(srcFile, destFile);
        srcFile.delete();
    }
