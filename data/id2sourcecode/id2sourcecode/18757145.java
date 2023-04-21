    private void copyLircdConf(URI resourcePath, ControllerConfiguration config) {
        File lircdConfFile = new File(resourcePath.resolve(Constants.LIRCD_CONF).getPath());
        File lircdconfDir = new File(config.getLircdconfPath().replaceAll(Constants.LIRCD_CONF, ""));
        try {
            if (lircdconfDir.exists() && lircdConfFile.exists()) {
                if (config.isCopyLircdconf()) {
                    FileUtils.copyFileToDirectory(lircdConfFile, lircdconfDir);
                    log.info("copy lircd.conf to" + config.getLircdconfPath());
                }
            }
        } catch (IOException e) {
            log.error("Can't copy lircd.conf to " + config.getLircdconfPath(), e);
        }
    }
