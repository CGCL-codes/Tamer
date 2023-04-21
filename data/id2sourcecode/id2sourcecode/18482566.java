    private void registerWithEnhydra(Properties prop) throws IOException, ServiceManagerException, com.lutris.util.ConfigException, com.lutris.appserver.server.ApplicationException {
        if (app == null) {
            app = new MyApplication();
            String configLocation = (String) prop.get("enhydra.config");
            app.setConfig((new ConfigFile(new File(configLocation))).getConfig());
            String logLocation = (String) prop.get("enhydra.logfile");
            File logFile = new File(logLocation);
            String[] levels = { "ERROR", "OSP_ERROR", "OSP_INFO", "OSP_DEBUG1", "OSP_DEBUG2", "OSP_DEBUG3" };
            StandardLogger logger = new StandardLogger(true);
            logger.configure(logFile, new String[] {}, levels);
            app.setLogChannel(logger.getChannel(""));
            app.startup(app.getConfig());
            Enhydra.register(app);
        }
    }
