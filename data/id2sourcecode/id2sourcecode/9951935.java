    @BeforeMethod
    public void beforeMethod() throws IOException {
        String currentdir = TestUtils.currentSourceDirName(this);
        inputFile = new File(currentdir + File.separator + "work.db4o");
        tempDir = new File("/home/dave/temp/" + getClass().getSimpleName());
        if (tempDir.exists()) {
            FileUtils.deleteDirectory(tempDir);
        }
        FileUtils.forceMkdir(tempDir);
        FileUtils.copyFileToDirectory(inputFile, tempDir);
        testFile = new File(tempDir.getAbsolutePath() + File.separator + "work.db4o");
    }
