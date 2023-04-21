    @PostConstruct
    protected void init() {
        URL url = this.getClass().getResource("/uk/icat3/sessionbeans/facility.properties");
        facilityProps = new Properties();
        String facilityLogFile = null;
        try {
            facilityProps.load(url.openStream());
            facilityLogFile = facilityProps.getProperty("facility.name");
        } catch (Exception mre) {
            facilityLogFile = "ISIS";
            System.out.println("Unable to load props file, setting log as  " + facilityLogFile + "\n" + mre);
        }
        FACILITY = facilityLogFile;
        PropertyConfigurator.configure(System.getProperty("user.home") + File.separator + "." + facilityLogFile + "-icatapi.properties");
        log.info("Loaded log4j properties from : " + System.getProperty("user.home") + File.separator + "." + facilityLogFile + "-icatapi.properties");
        File sessionConf = new File(System.getProperty("user.home") + File.separator + ".session.conf");
        if (sessionConf.exists()) {
            try {
                Properties props2 = new Properties();
                props2.load(new FileInputStream(sessionConf));
                String sessionImplementation = props2.getProperty("session.impl.class");
                log.info("Setting session implementation as: " + sessionImplementation);
                Constants.DEFAULT_USER_IMPLEMENTATION = sessionImplementation;
            } catch (Exception ex) {
                log.fatal("Session configuration is set up incorrectly.", ex);
                return;
            }
        }
    }
