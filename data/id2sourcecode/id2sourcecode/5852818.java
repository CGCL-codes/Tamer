    public void testTomcat() throws Exception {
        Configuration.init();
        TomcatServer ts = new TomcatServer();
        ts.registerServlet("/*", TestServlet.class.getName());
        ts.registerFilter("/*", StaticContentServletFilter.class.getName());
        ts.registerFilter("/*", TestFilter.class.getName());
        ts.start(5555);
        File startFolder = Configuration.codepackRoot("portal");
        XmlConfiguration config = Configuration.buildConfiguration(new File(startFolder, "test/resources/sample-config/packet1"));
        XmlConfiguration old = Configuration.replaceConfigurationWithTest(config);
        try {
            TestServlet.clearCalls();
            TestFilter.clearCalls();
            URL url = new URL("http://127.0.0.1:5555/a");
            URLConnection c = url.openConnection();
            InputStream is = c.getInputStream();
            is.close();
            assert TestServlet.calls() == 1;
            assert TestFilter.calls() == 1;
            url = new URL("http://127.0.0.1:5555/file1.txt");
            c = url.openConnection();
            assert c.getContentLength() == 3;
            url = new URL("http://127.0.0.1:5555/file2.txt");
            c = url.openConnection();
            assert c.getContentLength() == 6;
        } finally {
            Configuration.replaceConfigurationWithTest(old);
        }
    }
