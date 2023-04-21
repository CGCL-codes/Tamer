        public void setUp() throws Exception {
            String home = System.getProperty("java.io.tmpdir") + File.separator + getClass().getName() + "-" + System.currentTimeMillis();
            homeDir = new File(home, name);
            dataDir = new File(homeDir, "data");
            confDir = new File(homeDir, "conf");
            homeDir.mkdirs();
            dataDir.mkdirs();
            confDir.mkdirs();
            File f = new File(confDir, "solrconfig.xml");
            FileUtils.copyFile(new File(getSolrConfigFile()), f);
            f = new File(confDir, "schema.xml");
            FileUtils.copyFile(new File(getSchemaFile()), f);
        }
