    @Before
    public void setUp() throws Exception {
        tempDir = TestUtils.createTempDir(getClass());
        tempFile = new File(tempDir, "dummy_template.xml");
        FileUtils.copyFile(new File(resourceDir, "src" + File.separator + "dummy_template.xml"), tempFile);
    }
