    private void setCustomConfigDir() {
        String customConfigurationDir = System.getProperty(CONFIG_DIR_PROPERTY_NAME);
        File portableAppConfigDir = new File("PortMapperConf");
        if (customConfigurationDir != null) {
            File dir = new File(customConfigurationDir);
            if (!dir.isDirectory()) {
                logger.error("Custom configuration directory '" + customConfigurationDir + "' is not a directory.");
                System.exit(1);
            }
            if (!dir.canRead() || !dir.canWrite()) {
                logger.error("Can not read or write to custom configuration directory '" + customConfigurationDir + "'.");
                System.exit(1);
            }
            logger.info("Using custom configuration directory '" + dir.getAbsolutePath() + "'.");
            getContext().getLocalStorage().setDirectory(dir);
        } else if (portableAppConfigDir.isDirectory() && portableAppConfigDir.canRead() && portableAppConfigDir.canWrite()) {
            logger.info("Found portable app configuration directory '" + portableAppConfigDir.getAbsolutePath() + "'.");
            getContext().getLocalStorage().setDirectory(portableAppConfigDir);
        } else {
            logger.info("Using default configuration directory '" + getContext().getLocalStorage().getDirectory().getAbsolutePath() + "'.");
        }
    }
