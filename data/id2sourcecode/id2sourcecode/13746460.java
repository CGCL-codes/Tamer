    @Test
    public void fileCopy() throws IOException {
        File tempfile = File.createTempFile("jgnash-test", "jdb");
        String absolutepath = tempfile.getAbsolutePath();
        String testdata = "42";
        writeTestData(testdata, tempfile);
        checkTestData(testdata, absolutepath);
        File secondTempFile = File.createTempFile("jgnash-test", "jdb");
        if (FileUtils.copyFile(tempfile, secondTempFile)) {
            checkTestData(testdata, secondTempFile.getAbsolutePath());
        }
    }
