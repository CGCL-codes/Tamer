    public static void loadDefaultMetadataConfiguration() throws IOException {
        java.io.File conf_dir = new java.io.File(CommonUtils.getUserSettingsDir().getAbsolutePath());
        java.net.URL url = Thread.currentThread().getContextClassLoader().getResource("nmd.jar");
        if (!conf_dir.exists()) {
            try {
                ZipUtility.unZipURLToDirectory(url, CommonUtils.getUserSettingsDir().getAbsolutePath());
                LionShareApplicationSettings.NMD_CONF_LAST_MODIFIED.setValue(url.openConnection().getLastModified());
            } catch (Exception e) {
            }
        } else {
            if (LionShareApplicationSettings.NMD_CONF_LAST_MODIFIED.getValue() != url.openConnection().getLastModified()) {
                try {
                    ZipUtility.unZipURLToDirectory(url, CommonUtils.getUserSettingsDir().getAbsolutePath());
                } catch (Exception e) {
                }
                LionShareApplicationSettings.NMD_CONF_LAST_MODIFIED.setValue(url.openConnection().getLastModified());
            }
        }
    }
