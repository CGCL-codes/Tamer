    public synchronized void save() {
        if (!isSaveRequired) {
            logger.debug("No saving of preferences required.");
            return;
        }
        logger.debug("Saving preferences to: " + prefFile.getAbsolutePath());
        Properties saveProperties = new SortedProperties();
        for (Setting<?> setting : settingMap.values()) {
            if (setting.isDefault() && !setting.isAlwaysSaved()) {
                continue;
            }
            PreferencesCodec.serializeSetting(setting, saveProperties);
        }
        File bakFile = new File(prefFile.getParentFile(), prefFile.getName() + ".bak");
        try {
            if (prefFile.exists()) {
                FileUtils.copyFile(prefFile, bakFile);
            }
            OutputStream os = null;
            try {
                os = new BufferedOutputStream(new FileOutputStream(prefFile));
                saveProperties.store(os, "Phex Preferences");
            } finally {
                IOUtil.closeQuietly(os);
            }
        } catch (IOException exp) {
            logger.error(exp.toString(), exp);
        }
    }
