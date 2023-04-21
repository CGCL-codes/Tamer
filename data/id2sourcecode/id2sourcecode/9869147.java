    private String setConfigFile(String configFileName, String defaultConf) {
        String ret = "";
        if (new File(pathConf + "/" + configFileName).exists()) ret = FilenameUtils.normalize(pathConf + "/" + configFileName); else {
            try {
                FileUtils.copyFile(new File(defaultConf), new File(pathConf + "/" + configFileName));
            } catch (IOException e) {
                log.error("Copying configuration file: " + e.getMessage());
            }
            ret = FilenameUtils.normalize(pathConf + "/" + configFileName);
        }
        return ret;
    }
