    @Override
    public void execute(String[] args) throws Exception {
        logger.debug("*** execute ***");
        ExtensionPoint javaModuleExtensionPoint = getDescriptor().getExtensionPoint(EXTENSIONPOINT_COMPONENT);
        for (Extension extension : javaModuleExtensionPoint.getConnectedExtensions()) {
            File sourceJarFile = getFilePath(extension.getDeclaringPluginDescriptor(), extension.getParameter("jar").valueAsString());
            File myPluginDataDir = getPluginTmpDir(extension.getDeclaringPluginDescriptor());
            File destinationJarFile = new File(myPluginDataDir, sourceJarFile.getName());
            destinationJarFile.getParentFile().mkdirs();
            logger.debug("Copy " + sourceJarFile.getPath() + " to " + destinationJarFile);
            FileUtils.copyFile(sourceJarFile, destinationJarFile);
        }
    }
