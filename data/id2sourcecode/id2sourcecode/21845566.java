    @Test
    public void testCopyFile1ToDir() throws Exception {
        File directory = new File(getTestDirectory(), "subdir");
        if (!directory.exists()) directory.mkdirs();
        File destination = new File(directory, testFile1.getName());
        FileUtils.copyFileToDirectory(testFile1, directory);
        assertTrue("Check Exist", destination.exists());
        assertTrue("Check Full copy", destination.length() == testFile1Size);
        try {
            FileUtils.copyFileToDirectory(destination, directory);
            fail("Should not be able to copy a file into the same directory as itself");
        } catch (IOException ioe) {
        }
    }
