    @Deprecated
    protected void assembleWebInfLibs(PluginDescriptor pd) throws IOException {
        File myWARPluginDir = new File(getPluginTmpDir(), pd.getId());
        File destDir = new File(myWARPluginDir + "/WEB-INF/lib");
        destDir.getParentFile().mkdirs();
        for (ExtensionPoint extnPt : pd.getExtensionPoints()) {
            if (EXNPT_WEBINFLIB.equals(extnPt.getParentExtensionPointId()) && extnPt.getParentPluginId().equals(getDescriptor().getId())) {
                for (Extension exn : extnPt.getConnectedExtensions()) {
                    String srcName = exn.getParameter("jar").valueAsString();
                    File src = getFilePath(exn.getDeclaringPluginDescriptor(), srcName);
                    logger.debug("Copy " + src.getPath() + " to " + destDir.getPath());
                    FileUtils.copyFileToDirectory(src, destDir);
                }
            }
        }
    }
