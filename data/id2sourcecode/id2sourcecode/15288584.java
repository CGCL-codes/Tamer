    protected void assembleMessagingProduct() throws IOException {
        String destMessagingFilename = getDescriptor().getAttribute(ATTRIBUTE_DEST_MESSAGING).getValue();
        File destMessagingFile = new File(getPluginTmpDir(), destMessagingFilename);
        String sourceMessaging = getDescriptor().getAttribute(ATTRIBUTE_SOURCE_MESSAGING).getValue();
        File sourceMessagingFile = getFilePath(sourceMessaging);
        if (!destMessagingFile.exists() || sourceMessagingFile.lastModified() > destMessagingFile.lastModified()) {
            logger.debug(destMessagingFile.getPath() + " was replaced since its source files are more recent");
            logger.debug("Copy " + sourceMessagingFile.getPath() + " to " + destMessagingFile);
            FileUtils.copyFile(sourceMessagingFile, destMessagingFile);
        } else {
            logger.debug(destMessagingFile.getPath() + " is more recent than any of its source file: " + sourceMessagingFile.getPath());
        }
    }
