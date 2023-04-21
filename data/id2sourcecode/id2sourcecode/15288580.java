    protected void collectTolvenDSProduct() throws IOException {
        String destTolvenDSFilename = getDescriptor().getAttribute(ATTRIBUTE_DEST_TOLVENDS).getValue();
        File destTolvenDSFile = new File(getPluginTmpDir(), destTolvenDSFilename);
        ExtensionPoint tolvenDSProviderExtensionPoint = getMyExtensionPoint(EXTENSIONPOINT_TOLVENDS_PROVIDER);
        for (Extension tolvenDSProviderExtension : tolvenDSProviderExtensionPoint.getConnectedExtensions()) {
            String sourceTolvenDS = tolvenDSProviderExtension.getParameter("tolvenDS").valueAsString();
            File sourceTolvenDSPluginTmpDir = getPluginTmpDir(tolvenDSProviderExtension.getDeclaringPluginDescriptor());
            File sourceTolvenDSFile = new File(sourceTolvenDSPluginTmpDir, sourceTolvenDS);
            if (!destTolvenDSFile.exists() || sourceTolvenDSFile.lastModified() > destTolvenDSFile.lastModified()) {
                logger.debug(destTolvenDSFile.getPath() + " was replaced since its source files are more recent");
                logger.debug("Copy " + sourceTolvenDSFile.getPath() + " to " + destTolvenDSFile);
                FileUtils.copyFile(sourceTolvenDSFile, destTolvenDSFile);
            } else {
                logger.debug(destTolvenDSFile.getPath() + " is more recent than any of its source file: " + sourceTolvenDSFile.getPath());
            }
        }
    }
