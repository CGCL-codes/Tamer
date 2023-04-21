    public void init() throws MalformedURLException, ParseException, UnsupportedFormatException {
        File basePath = new File("/opt/cvs-co/informa/test/data");
        FileUtils.copyFile(new File(basePath, "xmlhack-0.91.xml"), new File("/tmp/dummy.xml"));
        String[] chl_locs = { "/tmp/dummy.xml" };
        ChannelBuilderIF builder = new ChannelBuilder();
        ChannelObserverIF observer = new MyChannelObserver();
        registry = new ChannelRegistry(builder);
        for (int i = 0; i < chl_locs.length; i++) {
            ChannelIF c = registry.addChannel(new File(chl_locs[i]).toURL(), 20, true);
            c.addObserver(observer);
        }
        Thread t = new MySimulator();
        t.start();
    }
