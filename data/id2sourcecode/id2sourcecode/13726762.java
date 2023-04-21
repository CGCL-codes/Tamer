    public SetupHelper() throws Exception {
        String testPropertiesName = System.getProperty("test.properties");
        if (testPropertiesName == null) {
            testPropertiesName = "test.properties";
            logger.info("Setting test.properties file name to default: test.properties");
        }
        TungstenProperties tp = new TungstenProperties();
        File f = new File(testPropertiesName);
        if (f.canRead()) {
            logger.info("Reading test.properties file: " + testPropertiesName);
            FileInputStream fis = new FileInputStream(f);
            tp.load(fis);
            fis.close();
        } else logger.warn("Using default values for test");
        nativeDriver = tp.getString("test.native.driver", "org.apache.derby.jdbc.EmbeddedDriver", true);
        readwriteUrl = tp.getString("test.readwrite.url", "jdbc:derby:readwrite;create=true", true);
        readonlyUrl = tp.getString("test.readonly.url", "jdbc:derby:readonly;create=true", true);
        user = tp.getString("test.database.user");
        password = tp.getString("test.database.password");
        dataServiceName = tp.getString("test.database.serviceName", DEFAULT_SERVICE_NAME, true);
        clusterHomeName = System.getProperty("cluster.home");
        if (clusterHomeName == null) throw new Exception("Property cluster.home is not set");
        createDefaultConfiguration("smoke-service");
        Column colDbtype = new Column();
        colDbtype.setName("dbtype");
        colDbtype.setType(Types.VARCHAR);
        colDbtype.setLength(40);
        Column[] cols = new Column[] { colDbtype };
        tableDbtype = new Table("dbtype_table", cols);
        createDbtypeTable(this.readwriteUrl, "rw");
        createDbtypeTable(this.readonlyUrl, "ro");
    }
