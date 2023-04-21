    private static File resolveConfigFile() throws IOException {
        File configDir = new File(SystemUtils.getUserHome(), CONFIG_DIR);
        if (!configDir.exists()) {
            if (!configDir.mkdir()) {
                throw new IOException("cannot read config directory: " + configDir.getAbsolutePath());
            }
        } else if (!configDir.canWrite()) {
            throw new IOException("no write access in config directory: " + configDir.getAbsolutePath());
        }
        File configFile = new File(configDir, CONFIG_FILE);
        if (!configFile.exists()) {
            File defaultConfig = new File(DEFAULT_CONFIG_FILE);
            FileUtils.copyFile(defaultConfig, configFile);
        }
        return configFile;
    }
