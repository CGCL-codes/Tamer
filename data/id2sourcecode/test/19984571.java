    public static void loadDefaultEclConfiguration() throws Exception {
        java.io.File conf_dir = new java.io.File(CommonUtils.getUserSettingsDir().getAbsolutePath() + java.io.File.separator + "conf");
        java.net.URL url = Thread.currentThread().getContextClassLoader().getResource("ecl_conf.jar");
        if (!conf_dir.exists()) {
            try {
                ZipUtility.unZipURLToDirectory(url, CommonUtils.getUserSettingsDir().getAbsolutePath());
                LionShareApplicationSettings.ECL_CONF_LAST_MODIFIED.setValue(url.openConnection().getLastModified());
            } catch (Exception e) {
            }
        } else {
            long nLastModified = url.openConnection().getLastModified();
            if (LionShareApplicationSettings.ECL_CONF_LAST_MODIFIED.getValue() != nLastModified) {
                try {
                    ZipUtility.unZipURLToDirectory(url, CommonUtils.getUserSettingsDir().getAbsolutePath());
                } catch (Exception e) {
                }
                LionShareApplicationSettings.ECL_CONF_LAST_MODIFIED.setValue(nLastModified);
            }
        }
    }
