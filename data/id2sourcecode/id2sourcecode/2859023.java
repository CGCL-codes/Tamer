    @Before
    public void setUp() throws IOException {
        final URL url = getClass().getResource("svntest.properties");
        workdir = new File(new File(url.getPath()).getParentFile(), "workdir");
        final Properties properties = new Properties();
        properties.load(url.openStream());
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            jobParams.put(entry.getKey() + "", entry.getValue() + "");
        }
        FileUtils.deleteDirectory(workdir);
        workdir.mkdirs();
    }
