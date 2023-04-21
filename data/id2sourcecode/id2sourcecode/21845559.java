    @Test
    public void testCopyFile2WithoutFileDatePreservation() throws Exception {
        File destination = new File(getTestDirectory(), "copy2.txt");
        FileUtils.copyFile(testFile1, destination, false);
        assertTrue("Check Exist", destination.exists());
        assertTrue("Check Full copy", destination.length() == testFile2Size);
    }
