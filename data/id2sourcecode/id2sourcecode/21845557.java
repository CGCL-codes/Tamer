    @Test
    public void testCopyFile2() throws Exception {
        File destination = new File(getTestDirectory(), "copy2.txt");
        FileUtils.copyFile(testFile1, destination);
        assertTrue("Check Exist", destination.exists());
        assertTrue("Check Full copy", destination.length() == testFile2Size);
    }
