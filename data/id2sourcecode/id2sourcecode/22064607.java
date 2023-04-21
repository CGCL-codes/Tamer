    @PostConstruct
    public void init() {
        URL url = this.getClass().getResource("/uk/ac/dl/dp/core/messages/facility.properties");
        Properties props = new Properties();
        String facilityLogFile = null;
        try {
            props.load(url.openStream());
            facilityLogFile = props.getProperty("facility.name");
        } catch (Exception mre) {
            facilityLogFile = "ISIS";
            System.out.println("Unable to load props file, setting log as  " + facilityLogFile + "\n" + mre);
        }
        if (new File(System.getProperty("user.home") + File.separator + "." + facilityLogFile + "-dp-core-log4j.xml").exists()) {
            PropertyConfigurator.configure(System.getProperty("user.home") + File.separator + "." + facilityLogFile + "-dp-core-log4j.xml");
        } else {
            PropertyConfigurator.configure(System.getProperty("user.home") + File.separator + "." + facilityLogFile + "-dp-core-log4j.properties");
        }
    }
