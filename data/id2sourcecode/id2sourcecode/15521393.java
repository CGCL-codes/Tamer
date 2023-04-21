    private void createJsystemPropertiesFile() {
        String home = System.getProperty("user.dir");
        File baseFile = new File(home, CommonResources.JSYSTEM_BASE_FILE);
        try {
            settingFile.createNewFile();
        } catch (IOException e1) {
            log.severe("Problem creating new properties file!");
        }
        if (baseFile.exists()) {
            try {
                FileUtils.copyFile(baseFile, settingFile);
                log.fine("Base file copied to jsystem properties file.");
            } catch (IOException e) {
                log.warning("Couldn't copy base file!");
            }
        }
    }
