    private void copyLircdConf(String resourcePath) {
        File lircdConfFile = new File(resourcePath + Constants.LIRCD_CONF);
        File lircdconfDir = new File(configuration.getLircdconfPath().replaceAll(Constants.LIRCD_CONF, ""));
        try {
            if (lircdconfDir.exists() && lircdConfFile.exists()) {
                if (configuration.isCopyLircdconf()) {
                    FileUtils.copyFileToDirectory(lircdConfFile, lircdconfDir);
                    logger.info("copy lircd.conf to" + configuration.getLircdconfPath());
                }
            }
        } catch (IOException e) {
            logger.error("Can't copy lircd.conf to " + configuration.getLircdconfPath(), e);
        }
    }
