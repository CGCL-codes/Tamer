    private static Object validate(final String configKey, final Number value) {
        int newValue = value.intValue();
        if (newValue < 0) {
            newValue = 0;
        }
        if (configKey == UPLOAD_CONFIGKEY) {
            final int downValue = COConfigurationManager.getIntParameter(DOWNLOAD_CONFIGKEY);
            if (newValue != 0 && newValue < COConfigurationManager.CONFIG_DEFAULT_MIN_MAX_UPLOAD_SPEED && (downValue == 0 || downValue > newValue * 2)) {
                newValue = (downValue + 1) / 2;
            }
        } else if (configKey == DOWNLOAD_CONFIGKEY) {
            final int upValue = COConfigurationManager.getIntParameter(UPLOAD_CONFIGKEY);
            if (upValue != 0 && upValue < COConfigurationManager.CONFIG_DEFAULT_MIN_MAX_UPLOAD_SPEED) {
                if (newValue > upValue * 2) {
                    newValue = upValue * 2;
                } else if (newValue == 0) {
                    newValue = upValue * 2;
                }
            }
        } else if (configKey == UPLOAD_SEEDING_CONFIGKEY) {
        } else {
            throw new IllegalArgumentException("Invalid Configuation Key; use key for max upload and max download");
        }
        return new Integer(newValue);
    }
