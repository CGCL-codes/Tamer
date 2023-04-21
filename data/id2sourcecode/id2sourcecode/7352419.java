    public CLAPI() throws Exception {
        String configFile = System.getProperty("org.openmolgrid.cli.config");
        if (configFile == null) configFile = "cli_config.txt";
        props = CommonTools.getCLIProperties(configFile);
        if (props == null) {
            throw new Exception("Error: Could not get CLI config file!");
        }
        String defaultsFile = System.getProperty("org.openmolgrid.cli.defaults");
        if (defaultsFile == null) {
            defaultsFile = "userdefaults.txt";
        }
        defaultsProps = CommonTools.getCLIProperties(defaultsFile);
        if (defaultsProps == null) {
            throw new Exception("Error: Could not get CLI defaults file!");
        }
        try {
            FileHandler fileHandler = new FileHandler(defaultsProps.getProperty("logger"));
            fileHandler.setFormatter(new SimpleFormatter());
            logger.setLevel(Level.INFO);
            logger.addHandler(fileHandler);
            logger.info("Logging started.");
            String logLevel = defaultsProps.getProperty("logging_level");
            Level level = null;
            try {
                level = Level.parse(logLevel);
            } catch (Exception ex) {
                logger.warning("Unrecognised loglevel, going to INFO");
                level = Level.INFO;
                logLevel = "INFO (fallback)";
            }
            logger.setLevel(level);
            logger.info("New log level " + logLevel);
        } catch (Exception ex) {
            throw new Exception("Could not create CLI logging: " + ex.getMessage());
        }
        keystore = defaultsProps.getProperty("keystore");
        if (keystore == null) {
            logger.severe("No keystore found!");
            throw new Exception("No keystore found!");
        }
        if (defaultsProps.containsKey("password")) {
            password = defaultsProps.getProperty("password");
        } else {
            String passwdPath = defaultsProps.getProperty("passwdfile");
            if (passwdPath == null) {
                logger.severe("No password found!");
                throw new Exception("No password found!");
            }
            try {
                String line = new String();
                File passwdFile = new File(passwdPath);
                BufferedReader r = new BufferedReader(new FileReader(passwdFile));
                while ((line += r.readLine()) != null) ;
                password = line.trim();
            } catch (IOException ex) {
                logger.severe("Error occured while retrieving password: " + ex.getMessage());
                throw new Exception("Error occured while retrieving password!");
            }
        }
        String unicoreDir = defaultsProps.getProperty("userDefaultsDir");
        System.setProperty("org.openmolgrid.cli.unicoredir", unicoreDir);
        if (!initResources()) {
            logger.severe("Error occured while initializing resources!");
            throw new Exception("Error occured while initializing resources!");
        }
    }
