    protected void assembleTolvenJMSProduct() throws IOException {
        String destTolvenJMSFilename = getDescriptor().getAttribute(ATTRIBUTE_DEST_TOLVENJMS).getValue();
        File destTolvenJMSFile = new File(getPluginTmpDir(), destTolvenJMSFilename);
        ExtensionPoint tolvenJMSProviderExtensionPoint = getMyExtensionPoint(EXTENSIONPOINT_TOLVENJMS_PROVIDER);
        for (Extension tolvenJMSProviderExtension : tolvenJMSProviderExtensionPoint.getConnectedExtensions()) {
            String sourceTolvenJMS = tolvenJMSProviderExtension.getParameter("tolvenJMS").valueAsString();
            File sourceTolvenJMSPluginTmpDir = getPluginTmpDir(tolvenJMSProviderExtension.getDeclaringPluginDescriptor());
            File sourceTolvenJMSFile = new File(sourceTolvenJMSPluginTmpDir, sourceTolvenJMS);
            if (!destTolvenJMSFile.exists() || sourceTolvenJMSFile.lastModified() > destTolvenJMSFile.lastModified()) {
                logger.debug(destTolvenJMSFile.getPath() + " was replaced since its source files are more recent");
                logger.debug("Copy " + sourceTolvenJMSFile.getPath() + " to " + destTolvenJMSFile);
                FileUtils.copyFile(sourceTolvenJMSFile, destTolvenJMSFile);
            } else {
                logger.debug(destTolvenJMSFile.getPath() + " is more recent than any of its source file: " + sourceTolvenJMSFile.getPath());
            }
        }
    }
