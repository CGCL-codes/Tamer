    @Test
    public void testCopyToSelf() throws Exception {
        File destination = new File(getTestDirectory(), "copy3.txt");
        FileUtils.copyFile(testFile1, destination);
        try {
            FileUtils.copyFile(destination, destination);
            fail("file copy to self should not be possible");
        } catch (IOException ioe) {
        }
    }
