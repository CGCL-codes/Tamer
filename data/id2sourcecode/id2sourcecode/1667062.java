    public void init(String fileLocation) throws IOException {
        if (fileLocation == null) {
            fileLocation = DEFAULT_CONFIGFILE_LOCATION;
        }
        File configFile = new File(fileLocation);
        if (!configFile.exists() || !configFile.isFile() || !configFile.canRead()) {
            throw new IOException("Error cannot read config file '" + fileLocation + "', check that it exist and is readable.");
        }
        configProperties.load(new FileInputStream(configFile));
        PropertyConfigurator.configure(configProperties);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        configProperties.store(baos, null);
        configData = baos.toByteArray();
    }
