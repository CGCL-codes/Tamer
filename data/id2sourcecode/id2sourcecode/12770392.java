    protected void collectTolvenJMSProduct() throws IOException {
        ExtensionPoint jBossComponentExtensionPoint = getDescriptor().getExtensionPoint(EXTENSIONPOINT_JBOSS_COMPONENT);
        for (Extension jBossComponentExtension : jBossComponentExtensionPoint.getConnectedExtensions()) {
            String destinationTolvenJMSFilename = getDescriptor().getAttribute("tolvenJMS").getValue();
            PluginDescriptor jBossComponentPluginDescriptor = jBossComponentExtension.getDeclaringPluginDescriptor();
            File destinationTolvenJMSFile = new File(getPluginTmpDir(jBossComponentPluginDescriptor), destinationTolvenJMSFilename);
            ExtensionPoint tolvenJMSProviderExtensionPoint = jBossComponentPluginDescriptor.getExtensionPoint(EXTENSIONPOINT_TOLVENJMS_PROVIDER);
            if (tolvenJMSProviderExtensionPoint == null) {
                throw new RuntimeException("ExtensionPoint '" + EXTENSIONPOINT_TOLVENJMS_PROVIDER + "' not found in " + jBossComponentPluginDescriptor.getId());
            }
            for (Extension tolvenJMSProviderExtension : tolvenJMSProviderExtensionPoint.getConnectedExtensions()) {
                String sourceTolvenJMS = tolvenJMSProviderExtension.getParameter("tolvenJMS").valueAsString();
                File sourceTolvenJMSPluginTmpDir = getPluginTmpDir(tolvenJMSProviderExtension.getDeclaringPluginDescriptor());
                File sourceTolvenJMSFile = new File(sourceTolvenJMSPluginTmpDir, sourceTolvenJMS);
                if (!destinationTolvenJMSFile.exists() || sourceTolvenJMSFile.lastModified() > destinationTolvenJMSFile.lastModified()) {
                    logger.debug(destinationTolvenJMSFile.getPath() + " was replaced since its source files are more recent");
                    logger.debug("Copy " + sourceTolvenJMSFile.getPath() + " to " + destinationTolvenJMSFile);
                    FileUtils.copyFile(sourceTolvenJMSFile, destinationTolvenJMSFile);
                } else {
                    logger.debug(destinationTolvenJMSFile.getPath() + " is more recent than any of its source file: " + sourceTolvenJMSFile.getPath());
                }
            }
        }
    }
