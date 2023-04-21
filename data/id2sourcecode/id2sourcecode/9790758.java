    public static void main(String[] args) {
        CLI cli = new CLI();
        String configProperties = System.getProperty(("org.openmolgrid.cli.config"));
        if (configProperties == null) configProperties = "cli_config.txt";
        Properties props = CommonTools.getCLIProperties(configProperties);
        try {
            String defaultsProps = System.getProperty(("org.openmolgrid.cli.defaults"));
            if (defaultsProps == null) defaultsProps = "userdefaults.txt";
            Properties defProps = CommonTools.getCLIProperties(defaultsProps);
            FileHandler fileHandler = new FileHandler(defProps.getProperty("logger"));
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.INFO);
            logger.info("Logging started.");
            String logLevel = defProps.getProperty("logging_level");
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
            String email = defProps.getProperty("userEmail", "");
            System.setProperty("cli.userEmail", email);
        } catch (IOException ex) {
            System.out.println("Could not create CLI logging: " + ex.getMessage());
            return;
        }
        String classname = props.getProperty(args[0]);
        if (classname == null || classname.equals("")) {
            logger.severe("Unknown operation " + args[0]);
            System.out.println("Unknown operation " + args[0]);
            return;
        }
        logger.info("loading class: " + classname);
        System.out.println("classname: " + classname);
        Object o = null;
        try {
            Class c = Class.forName(classname);
            o = c.newInstance();
        } catch (Exception e) {
            logger.severe("Could not load class " + classname);
            System.out.println("Could not load class " + classname);
            return;
        }
        if (!(o instanceof AbstractClientInterface)) {
            logger.severe("Wrong class type: " + classname);
            System.out.println("Wrong class type: " + classname);
            return;
        }
        String[] options = new String[args.length - 1];
        for (int i = 0; i < args.length - 1; ++i) options[i] = args[i + 1];
        try {
            ((AbstractClientInterface) o).setOptions(options);
            ((AbstractClientInterface) o).process();
        } catch (Exception ex) {
            logger.severe("Error occured!");
            logger.severe(ex.getMessage());
            System.err.println("Error occured!");
            ex.printStackTrace();
            return;
        }
    }
