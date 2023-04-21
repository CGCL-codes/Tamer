    public static synchronized void configure() {
        InputStream inputStream = null;
        try {
            if (INITIALISED) {
                return;
            }
            INITIALISED = Boolean.TRUE;
            try {
                URL url = Logging.class.getResource(LOG_4_J_PROPERTIES);
                System.out.println(Logging.class.getName() + " Log4j url : " + url);
                if (url != null) {
                    inputStream = url.openStream();
                } else {
                    inputStream = Logging.class.getResourceAsStream(LOG_4_J_PROPERTIES);
                    System.err.println("Input stream to logging configuration : " + inputStream);
                    if (inputStream == null) {
                        inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(LOG_4_J_PROPERTIES);
                    }
                    if (inputStream == null) {
                        File logFile = FileUtilities.findFileRecursively(new File("."), LOG_4_J_PROPERTIES);
                        if (logFile != null && logFile.exists() && logFile.canRead()) {
                            inputStream = logFile.toURI().toURL().openStream();
                        }
                    }
                }
                if (inputStream != null) {
                    Properties properties = new Properties();
                    properties.load(inputStream);
                    PropertyConfigurator.configure(properties);
                } else {
                    System.err.println("Logging properties file not found : " + LOG_4_J_PROPERTIES);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            LOGGER = Logger.getLogger(Logging.class);
            try {
                if (LOG_FILE == null) {
                    LOGGER.info("Searching for log file : " + IConstants.IKUBE_LOG);
                    LOG_FILE = FileUtilities.findFileRecursively(new File("."), IConstants.IKUBE_LOG);
                    if (LOG_FILE != null) {
                        LOGGER.info("Found log file : " + LOG_FILE.getAbsolutePath());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            FileUtilities.close(inputStream);
            Logging.class.notifyAll();
        }
    }
