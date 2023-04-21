    private File getPluginDirectory() {
        String absoluteParent = new File(".").getAbsolutePath();
        absoluteParent = absoluteParent.substring(0, absoluteParent.length() - 1);
        File parentDirectory = new File(absoluteParent);
        if (parentDirectory.exists()) {
            File pluginDirectory = new File(parentDirectory, "PyramusPlugins");
            if (pluginDirectory.exists()) {
                if (pluginDirectory.canRead() && pluginDirectory.canWrite()) {
                    return pluginDirectory;
                } else {
                    throw new PluginManagerException("Cannot read or write into plugin directory");
                }
            } else {
                if (parentDirectory.canWrite()) {
                    if (!pluginDirectory.mkdir()) {
                        throw new PluginManagerException("Failed to create new plugin directory");
                    } else {
                        return pluginDirectory;
                    }
                } else {
                    throw new PluginManagerException("Unable to create new plugin directory. Parent folder is write protected");
                }
            }
        } else {
            throw new PluginManagerException("Plugins parent directory does not exist");
        }
    }
