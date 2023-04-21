    @Test
    public void testCopyFile1() throws Exception {
        File destination = new File(getTestDirectory(), "copy1.txt");
        FileUtils.copyFile(testFile1, destination);
        assertTrue("Check Exist", destination.exists());
        assertTrue("Check Full copy", destination.length() == testFile1Size);
    }
