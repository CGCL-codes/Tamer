    public LoggedTestCase(String name, String propertyFile) {
        super(name);
        this.isLog4jInClasspath = true;
        try {
            Class aClass = Class.forName("org.apache.log4j.Category");
            this.category = Category.getInstance(getClass().getName());
            Properties properties = new Properties();
            setProperties(properties);
            try {
                URL url = this.getClass().getResource("/" + propertyFile);
                if (url != null) {
                    InputStream is = url.openStream();
                    properties.load(is);
                    is.close();
                    PropertyConfigurator.configure(properties);
                    debug("Log4J successfully instantiated.");
                } else {
                    System.err.println("ERROR: cannot find " + propertyFile + " in the classpath!");
                }
            } catch (IOException e) {
                System.err.println("ERROR: cannot load " + propertyFile + "!");
            }
        } catch (ClassNotFoundException e) {
            this.isLog4jInClasspath = false;
            debug("Log4J instantiation failed. Using stdout.");
        }
    }
