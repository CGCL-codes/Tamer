    protected void assembleConfigFiles() throws IOException {
        ExtensionPoint exnPt = getDescriptor().getExtensionPoint(EXNPT_CONFIG);
        ExtensionPoint appServerExnPt = getParentExtensionPoint(exnPt);
        String relativeConfigExtDirPath = getDescriptor().getAttribute(ATTR_STAGE_CONFIG_DIR).getValue();
        File destDir = new File(getPluginTmpDir(), getAppServerDirname() + "/" + relativeConfigExtDirPath);
        for (Extension exn : appServerExnPt.getConnectedExtensions()) {
            PluginDescriptor pd = exn.getDeclaringPluginDescriptor();
            for (Parameter param : exn.getParameters("file")) {
                File src = getFilePath(pd, param.valueAsString());
                FileUtils.copyFileToDirectory(src, destDir);
            }
            for (Parameter param : exn.getParameters("dir")) {
                File srcDir = getFilePath(pd, param.valueAsString());
                FileUtils.copyDirectory(srcDir, destDir);
            }
        }
    }
