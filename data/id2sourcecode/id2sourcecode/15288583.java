    protected void assembleJMSDSProduct() throws IOException {
        String destJMSDSFilename = getDescriptor().getAttribute(ATTRIBUTE_DEST_JMSDS).getValue();
        File destJMSDSFile = new File(getPluginTmpDir(), destJMSDSFilename);
        String sourceJMSDS = getDescriptor().getAttribute(ATTRIBUTE_SOURCE_JMSDS).getValue();
        File sourceJMSDSFile = getFilePath(sourceJMSDS);
        if (!destJMSDSFile.exists() || sourceJMSDSFile.lastModified() > destJMSDSFile.lastModified()) {
            logger.debug(destJMSDSFile.getPath() + " was replaced since its source files are more recent");
            logger.debug("Copy " + sourceJMSDSFile.getPath() + " to " + destJMSDSFile);
            FileUtils.copyFile(sourceJMSDSFile, destJMSDSFile);
        } else {
            logger.debug(destJMSDSFile.getPath() + " is more recent than any of its source file: " + sourceJMSDSFile.getPath());
        }
    }
