    @Override
    public void setUp() throws Exception {
        super.setUp();
        File home = dataDir;
        File homeDir = new File(home, "example");
        File dataDir = new File(homeDir, "data");
        File confDir = new File(homeDir, "conf");
        homeDir.mkdirs();
        dataDir.mkdirs();
        confDir.mkdirs();
        File f = new File(confDir, "solrconfig.xml");
        String fname = "." + File.separator + "solr" + File.separator + "conf" + File.separator + "solrconfig-slave1.xml";
        FileUtils.copyFile(new File(fname), f);
        f = new File(confDir, "schema.xml");
        fname = "." + File.separator + "solr" + File.separator + "conf" + File.separator + "schema-binaryfield.xml";
        FileUtils.copyFile(new File(fname), f);
        jetty = new JettySolrRunner("/solr", port);
        System.setProperty("solr.solr.home", homeDir.getAbsolutePath());
        System.setProperty("solr.data.dir", dataDir.getAbsolutePath());
        jetty.start();
        jetty = new JettySolrRunner(context, 0);
        jetty.start();
        port = jetty.getLocalPort();
        String url = "http://localhost:" + jetty.getLocalPort() + context;
        server = new CommonsHttpSolrServer(url);
        super.postSetUp();
    }
