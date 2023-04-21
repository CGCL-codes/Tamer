    public static SynchronizerGuiPlugin loadPlugin(File file) throws PluginException {
        if (!file.isFile() || !FileUtils.getExtention(file.getAbsolutePath()).equals("jar")) return null;
        try {
            File tmpFile = File.createTempFile("taskunifier_plugin_", ".jar");
            try {
                tmpFile.deleteOnExit();
            } catch (Throwable t) {
            }
            org.apache.commons.io.FileUtils.copyFile(file, tmpFile);
            List<SynchronizerGuiPlugin> plugins = Main.getApiPlugins().loadJar(tmpFile, file, false);
            if (plugins.size() == 0) {
                try {
                    file.delete();
                } catch (Throwable t) {
                }
                throw new PluginException(PluginExceptionType.NO_VALID_PLUGIN);
            }
            if (plugins.size() > 1) {
                try {
                    file.delete();
                } catch (Throwable t) {
                }
                throw new PluginException(PluginExceptionType.MORE_THAN_ONE_PLUGIN);
            }
            try {
                if (!EqualsUtils.equals(Constants.PLUGIN_API_VERSION, plugins.get(0).getPluginApiVersion())) throw new PluginException(PluginExceptionType.OUTDATED_PLUGIN);
            } catch (PluginException pexc) {
                try {
                    file.delete();
                } catch (Throwable t) {
                }
                throw pexc;
            } catch (Throwable t1) {
                try {
                    file.delete();
                } catch (Throwable t2) {
                }
                throw new PluginException(PluginExceptionType.OUTDATED_PLUGIN);
            }
            SynchronizerGuiPlugin plugin = plugins.get(0);
            List<SynchronizerGuiPlugin> existingPlugins = new ArrayList<SynchronizerGuiPlugin>(Main.getApiPlugins().getPlugins());
            Main.getApiPlugins().addPlugin(file, plugin);
            GuiLogger.getLogger().info("Plugin loaded: " + plugin.getName() + " - " + plugin.getVersion());
            SynchronizerGuiPlugin loadedPlugin = plugin;
            for (SynchronizerGuiPlugin p : existingPlugins) {
                if (EqualsUtils.equals(p.getId(), plugin.getId())) {
                    SynchronizerGuiPlugin pluginToDelete = null;
                    if (CompareUtils.compare(p.getVersion(), plugin.getVersion()) < 0) {
                        pluginToDelete = p;
                    } else {
                        pluginToDelete = plugin;
                        loadedPlugin = null;
                    }
                    deletePlugin(pluginToDelete);
                    break;
                }
            }
            if (loadedPlugin != null) loadedPlugin.loadPlugin();
            return loadedPlugin;
        } catch (PluginException e) {
            throw e;
        } catch (Throwable t) {
            PluginLogger.getLogger().log(Level.WARNING, "Cannot install plugin", t);
            throw new PluginException(PluginExceptionType.ERROR_INSTALL_PLUGIN, t);
        }
    }
