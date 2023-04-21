    @BeforeClass
    public static void setUp() throws IOException {
        tempDir = TestUtils.createTempDir(TestConrefPushParser.class);
        inputFile = new File(tempDir, "conrefpush_stub.xml");
        FileUtils.copyFile(new File(srcDir, "conrefpush_stub.xml"), inputFile);
        FileUtils.copyFile(new File(srcDir, "conrefpush_stub2.xml"), new File(tempDir, "conrefpush_stub2.xml"));
    }
