    @Override
    public void execute(String[] args) throws Exception {
        logger.debug("*** execute ***");
        CommandLine commandLine = getCommandLine(args);
        String libPluginId = commandLine.getOptionValue(CMD_LINE_LIB_PLUGIN_OPTION);
        String destDirname = commandLine.getOptionValue(CMD_LINE_DESTDIR_OPTION);
        if (destDirname == null) {
            destDirname = new File(getStageDir(), libPluginId).getPath();
        }
        File destDir = new File(destDirname);
        destDir.mkdirs();
        PluginDescriptor libPluginDescriptor = getManager().getRegistry().getPluginDescriptor(libPluginId);
        ExtensionPoint abstractExtensionPoint = getAbstractLibPluginDescriptor().getExtensionPoint("lib");
        for (Extension libExtension : abstractExtensionPoint.getConnectedExtensions()) {
            if (libExtension.getDeclaringPluginDescriptor().getId().equals(libPluginDescriptor.getId())) {
                String libFilename = libExtension.getParameter("lib").valueAsString();
                File sourceLib = getFilePath(libPluginDescriptor, libFilename);
                logger.debug("Copy " + sourceLib.getPath() + " to " + destDir.getPath());
                FileUtils.copyFileToDirectory(sourceLib, destDir);
            }
        }
    }
