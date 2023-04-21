    public DBTest(String name) {
        super(name);
        if (!isInitiated) {
            isInitiated = true;
            try {
                MyApplication app = new MyApplication();
                File logFile = new File("test.log");
                String[] levels = { "HAMBO_ERROR", "HAMBO_INFO", "HAMBO_DEBUG1", "HAMBO_DEBUG2", "HAMBO_DEBUG3" };
                StandardLogger logger = new StandardLogger(true);
                logger.configure(logFile, new String[] {}, levels);
                app.setConfig((new ConfigFile(new File("test/Test.conf"))).getConfig());
                app.setLogChannel(logger.getChannel(""));
                app.startup(app.getConfig());
                Enhydra.register(app);
                Properties prop = new Properties();
                prop.put("loadOrder", "log database");
                prop.put("service.log.class", "hambo.svc.log.LogServiceManager");
                prop.put("service.log.factory", "hambo.svc.log.enhydra.EnhydraLogServiceFactory");
                prop.put("service.log.id", "our/log");
                prop.put("service.database.class", "hambo.svc.database.DBServiceManager");
                prop.put("service.database.factory", "hambo.svc.database.enhydra.EnhydraDBServiceFactory");
                prop.put("service.database.id", "our/db");
                ServiceManagerLoader loader = new ServiceManagerLoader(prop);
                loader.loadServices();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
