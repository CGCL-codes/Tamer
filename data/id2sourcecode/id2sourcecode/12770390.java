    protected void collectTolvenDSProduct() throws IOException {
        ExtensionPoint jBossComponentExtensionPoint = getDescriptor().getExtensionPoint(EXTENSIONPOINT_JBOSS_COMPONENT);
        for (Extension jBossComponentExtension : jBossComponentExtensionPoint.getConnectedExtensions()) {
            String destinationTolvenDSFilename = getDescriptor().getAttribute("tolvenDS").getValue();
            PluginDescriptor jBossComponentPluginDescriptor = jBossComponentExtension.getDeclaringPluginDescriptor();
            File destinationTolvenDSFile = new File(getPluginTmpDir(jBossComponentPluginDescriptor), destinationTolvenDSFilename);
            ExtensionPoint tolvenDSProviderExtensionPoint = jBossComponentPluginDescriptor.getExtensionPoint(EXTENSIONPOINT_TOLVENDS_PROVIDER);
            if (tolvenDSProviderExtensionPoint == null) {
                throw new RuntimeException("ExtensionPoint '" + EXTENSIONPOINT_TOLVENDS_PROVIDER + "' not found in " + jBossComponentPluginDescriptor.getId());
            }
            for (Extension tolvenDSProviderExtension : tolvenDSProviderExtensionPoint.getConnectedExtensions()) {
                String sourceTolvenDS = tolvenDSProviderExtension.getParameter("tolvenDS").valueAsString();
                File sourceTolvenDSPluginTmpDir = getPluginTmpDir(tolvenDSProviderExtension.getDeclaringPluginDescriptor());
                File sourceTolvenDSFile = new File(sourceTolvenDSPluginTmpDir, sourceTolvenDS);
                if (!destinationTolvenDSFile.exists() || sourceTolvenDSFile.lastModified() > destinationTolvenDSFile.lastModified()) {
                    logger.debug(destinationTolvenDSFile.getPath() + " was replaced since its source files are more recent");
                    logger.debug("Copy " + sourceTolvenDSFile.getPath() + " to " + destinationTolvenDSFile);
                    FileUtils.copyFile(sourceTolvenDSFile, destinationTolvenDSFile);
                } else {
                    logger.debug(destinationTolvenDSFile.getPath() + " is more recent than any of its source file: " + sourceTolvenDSFile.getPath());
                }
            }
        }
    }
