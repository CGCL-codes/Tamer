    protected void assembleLibProductAdaptors() throws IOException {
        ExtensionPoint exnPt = getDescriptor().getExtensionPoint(EXNPT_LIBPROD_ADPTR);
        ExtensionPoint appServerExnPt = getParentExtensionPoint(exnPt);
        String relativeLibExtDirPath = getDescriptor().getAttribute(ATTR_STAGE_LIB).getValue();
        File destDir = new File(getPluginTmpDir(), getAppServerDirname() + "/" + relativeLibExtDirPath);
        for (Extension exn : appServerExnPt.getConnectedExtensions()) {
            for (File src : getAdaptorFiles(exn)) {
                FileUtils.copyFileToDirectory(src, destDir);
            }
        }
    }
