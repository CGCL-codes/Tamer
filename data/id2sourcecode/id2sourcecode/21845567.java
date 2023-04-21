    @Test
    public void testCopyFile2ToDir() throws Exception {
        File directory = new File(getTestDirectory(), "subdir");
        if (!directory.exists()) directory.mkdirs();
        File destination = new File(directory, testFile1.getName());
        FileUtils.copyFileToDirectory(testFile1, directory);
        assertTrue("Check Exist", destination.exists());
        assertTrue("Check Full copy", destination.length() == testFile2Size);
    }
