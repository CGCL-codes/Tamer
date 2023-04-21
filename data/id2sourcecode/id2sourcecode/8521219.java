    @Override
    public void execute(String[] args) throws Exception {
        logger.debug("*** execute ***");
        CommandLine commandLine = getCommandLine(args);
        String rarPluginId = commandLine.getOptionValue(CMD_LINE_RAR_PLUGIN_OPTION);
        String destDirname = commandLine.getOptionValue(CMD_LINE_DESTDIR_OPTION);
        if (destDirname == null) {
            destDirname = new File(getStageDir(), rarPluginId).getPath();
        }
        File destDir = new File(destDirname);
        destDir.mkdirs();
        PluginDescriptor rarPluginDescriptor = getManager().getRegistry().getPluginDescriptor(rarPluginId);
        ExtensionPoint abstractExtensionPoint = getAbstractRARPluginDescriptor().getExtensionPoint("rar");
        for (Extension rarExtension : abstractExtensionPoint.getConnectedExtensions()) {
            if (rarExtension.getDeclaringPluginDescriptor().getId().equals(rarPluginDescriptor.getId())) {
                String rarFilename = rarExtension.getParameter("rar").valueAsString();
                File sourceRAR = getFilePath(rarPluginDescriptor, rarFilename);
                logger.debug("Copy " + sourceRAR.getPath() + " to " + destDir.getPath());
                FileUtils.copyFileToDirectory(sourceRAR, destDir);
            }
        }
    }
