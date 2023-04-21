    protected void assembleWebInfLibAdaptors(PluginDescriptor pd, String contextId) throws IOException {
        ExtensionPoint exnPt = pd.getExtensionPoint(EXNPT_WEBINFLIB_ADPTR);
        if (exnPt != null) {
            File myWARPluginDir = new File(getPluginTmpDir(), pd.getId());
            File destDir = new File(myWARPluginDir + "/WEB-INF/lib");
            destDir.getParentFile().mkdirs();
            for (Extension exn : exnPt.getConnectedExtensions()) {
                for (File src : getAdaptorFiles(exn)) {
                    logger.debug("Copy " + src.getPath() + " to " + destDir.getPath());
                    FileUtils.copyFileToDirectory(src, destDir);
                }
            }
        }
    }
