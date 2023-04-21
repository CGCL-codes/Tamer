    public void replaceOriginalByObfuscated(Set<String> directoriesToDelete) throws ObclipseException {
        File obfPluginDir = new File(ObclipseProps.get(ObclipseProps.APP_PLUGIN_DIR) + ObclipseProps.get(ObclipseProps.OBFUSCATED_TEMP_DIR));
        if (obfPluginDir.exists()) {
            File[] obfPlugins = obfPluginDir.listFiles();
            for (File obfPlugin : obfPlugins) {
                for (String dirToDelete : directoriesToDelete) {
                    File orgFile = new File(ObclipseProps.get(ObclipseProps.APP_PLUGIN_DIR) + obfPlugin.getName() + File.separator + dirToDelete);
                    FileOperations.deleteDir(orgFile);
                }
                File[] obfFiles = obfPlugin.listFiles();
                for (File obfFile : obfFiles) {
                    File orgFile = new File(ObclipseProps.get(ObclipseProps.APP_PLUGIN_DIR) + obfPlugin.getName() + File.separator + obfFile.getName());
                    if (obfFile.isDirectory()) {
                        FileOperations.copyDir(obfFile, orgFile);
                    } else {
                        FileOperations.copyFile(obfFile, orgFile);
                    }
                }
            }
            if (!FileOperations.deleteDir(obfPluginDir)) {
                Msg.error("Cannot delete obfuscation temp directory ''{0}''!", obfPluginDir);
            }
        }
    }
