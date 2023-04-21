    protected void copyResourceFile(final String fileName, final String destinationDirectory) {
        final String originalFile = configResourcesPath + fileName;
        final File srcFile = new File(originalFile);
        final String newFile = destinationDirectory + fileName;
        final File destFile = new File(newFile);
        try {
            FileUtils.copyFile(srcFile, destFile);
            if (log.isInfoEnabled()) {
                log.info("Copied file " + fileName + " to " + destFile.getAbsolutePath());
            }
        } catch (IOException e) {
            if (log.isWarnEnabled()) {
                log.warn("Failed to copy resource file: " + fileName);
            }
        }
    }
