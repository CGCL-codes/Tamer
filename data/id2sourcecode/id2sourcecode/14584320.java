    public void testXaDataPath_a21_013() throws Exception {
        final File testFile = new File(getDataFolder(), "a21_013_dp_pos_ix.xml");
        Assert.assertTrue("data_path target file not found.", testFile.canRead());
        final File fileInCurrentDir = new File(".", "a21_013_dp_pos_ix.xml");
        Assert.assertTrue("Cannot write test file to current directory", testFile.canWrite());
        clearInputParams();
        setInputXmlFileName(null);
        setBizDocFileName("a21_013_dp_pos.xbd");
        setExpectedOutputFileName("a21_013_dp_pos.xml");
        getTestHelper().setTestMethodName("testXaDataPath_a21_013");
        try {
            FileUtils.copyFile(testFile, fileInCurrentDir);
            evaluateBizDoc();
        } finally {
            fileInCurrentDir.delete();
        }
    }
