    public static void copyFile(File srcFile, File destFile) {
        try {
            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            LOGGER.error("Copy file from " + srcFile + " to " + destFile + " failed!", e);
        }
    }
