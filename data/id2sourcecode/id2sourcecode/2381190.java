    public ClientDB(long employeeId) throws IOException {
        super("sa", "", "");
        String baseFilename = SharedGlobal.APP_HOME_DIR + "db-" + Long.toString(employeeId);
        File scriptFile = new File(baseFilename + ".script");
        File propFile = new File(baseFilename + ".properties");
        if (!scriptFile.exists() || !propFile.exists()) {
            FileUtils.copyFile(getClass().getResourceAsStream(Global.DEFAULT_DB_SCRIPT), scriptFile);
            FileUtils.copyFile(getClass().getResourceAsStream(Global.DEFAULT_DB_PROPERTIES), propFile);
        }
        filename = baseFilename;
    }
