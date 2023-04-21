    public Object run(Object args) throws Exception {
        URL url = Platform.getBundle("ru.spbu.dorms.geo.rmp").getResource("dbconfig.properties");
        Properties dbProperties = new Properties();
        InputStream is = url.openStream();
        dbProperties.load(is);
        is.close();
        String path = dbProperties.getProperty("db.path");
        if (path == null) {
            throw new RuntimeException("dbconfig.properties does not contain db.path");
        }
        DBAccess dbInit = new DBAccess(path);
        Display display = PlatformUI.createDisplay();
        try {
            int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
            if (returnCode == PlatformUI.RETURN_RESTART) {
                return IPlatformRunnable.EXIT_RESTART;
            }
            return IPlatformRunnable.EXIT_OK;
        } finally {
            dbInit.shutdown();
            display.dispose();
        }
    }
