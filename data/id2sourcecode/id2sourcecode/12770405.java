    protected void assembleLibJars() throws IOException {
        ExtensionPoint extensionPoint = getDescriptor().getExtensionPoint(EXTENSIONPOINT_LIBJAR_COMPONENT);
        ExtensionPoint appServerExtensionPoint = getParentExtensionPoint(extensionPoint);
        String appserverHome = (String) evaluate("#{globalProperty['appserver.home']}", getDescriptor());
        File appserverHomeDir = new File(appserverHome);
        File stageDirAppserverHomeDir = new File(getStageDir(), appserverHomeDir.getName());
        File stageAppserverLibDir = new File(stageDirAppserverHomeDir, LIBDIR);
        for (Extension extension : appServerExtensionPoint.getConnectedExtensions()) {
            PluginDescriptor pluginDescriptor = extension.getDeclaringPluginDescriptor();
            for (Parameter parameter : extension.getParameters("jar")) {
                File sourceJar = getFilePath(pluginDescriptor, parameter.valueAsString());
                File destJar = new File(stageAppserverLibDir, sourceJar.getName());
                FileUtils.copyFile(sourceJar, destJar);
            }
        }
    }
