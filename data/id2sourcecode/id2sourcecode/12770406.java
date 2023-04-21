    protected void assembleLibClasses() throws IOException {
        File jar = new File(getPluginTmpDir(), getDescriptor().getId() + ".jar");
        jar.delete();
        ExtensionPoint classesExtensionPoint = getDescriptor().getExtensionPoint(EXTENSIONPOINT_LIB_CLASSES);
        for (Extension classesExtension : classesExtensionPoint.getConnectedExtensions()) {
            PluginDescriptor pluginDescriptor = classesExtension.getDeclaringPluginDescriptor();
            String dirname = classesExtension.getParameter("dir").valueAsString();
            String eval_dirname = (String) evaluate(dirname, pluginDescriptor);
            if (eval_dirname == null) {
                throw new RuntimeException("plugin property: dir '" + dirname + "'evaluated to: null for: " + pluginDescriptor);
            }
            File dir = getFilePath(pluginDescriptor, dirname);
            TolvenJar.jarDir(dir, jar, true);
        }
        if (jar.exists()) {
            String appserverHome = (String) evaluate("#{globalProperty['appserver.home']}", getDescriptor());
            File appserverHomeDir = new File(appserverHome);
            File stageDirAppserverHomeDir = new File(getStageDir(), appserverHomeDir.getName());
            File stageAppserverLibDir = new File(stageDirAppserverHomeDir, LIBDIR);
            logger.debug("Copy " + jar.getPath() + " to " + stageAppserverLibDir.getPath());
            FileUtils.copyFileToDirectory(jar, stageAppserverLibDir, true);
        }
    }
