    @Test
    public void fileCopyToSelf() throws IOException {
        File tempfile = File.createTempFile("jgnash-test", "jdb");
        String absolutepath = tempfile.getAbsolutePath();
        String testdata = "42";
        writeTestData(testdata, tempfile);
        checkTestData(testdata, absolutepath);
        assertFalse(FileUtils.copyFile(new File(absolutepath), new File(absolutepath)));
    }
