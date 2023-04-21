    private void installPluginFromFS(File file) {
        String filename = file.getName();
        File destFile = new File("." + File.separator + PLUGIN_DIRECTORY + File.separator + filename);
        if (destFile.exists()) {
            if (dialogs != null) if (!dialogs.overwriteFile(filename)) {
                return;
            }
        }
        try {
            FileUtils.copyFile(file, destFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PlugInDescriptor selectedPd = null;
        ;
        try {
            selectedPd = pluginManager.installPlugin(destFile);
        } catch (PopeyeException e) {
            dialogs.showErrorMessage(e.getMessage());
        }
        try {
            pluginManager.instantiatePlugin(selectedPd, plugDataWin.getMainPanel(), plugDataWin.getPlugManager().getWorkspace().getGroup().getWorkgroupName());
        } catch (PopeyeException e) {
            dialogs.showErrorMessage(e.getMessage());
            dialogs.showErrorMessage(e.getStackTrace().toString());
        }
    }
