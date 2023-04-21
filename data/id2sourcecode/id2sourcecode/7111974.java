    public TestApplication(Properties prop) throws HamboException {
        try {
            String configLocation = (String) prop.get("enhydra.config");
            setConfig((new ConfigFile(new File(configLocation))).getConfig());
            String logLocation = (String) prop.get("enhydra.logfile");
            File logFile = new File(logLocation);
            String[] levels = { "HAMBO_ERROR", "HAMBO_INFO", "HAMBO_DEBUG1", "HAMBO_DEBUG2", "HAMBO_DEBUG3" };
            StandardLogger logger = new StandardLogger(true);
            logger.configure(logFile, new String[] {}, levels);
            setLogChannel(logger.getChannel(""));
            startup(getConfig());
            register();
            ServiceManagerLoader loader = new ServiceManagerLoader(prop);
            loader.loadServices();
        } catch (IOException err) {
            throw new HamboException("I/O error in startup", err);
        } catch (ConfigException err) {
            throw new HamboException("Config error in startup", err);
        } catch (ApplicationException err) {
            throw new HamboException("Application error in startup", err);
        }
    }
