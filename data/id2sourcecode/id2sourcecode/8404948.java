    public void testCopyFile() throws IOException {
        File testFile = null;
        File destFile = null;
        try {
            testFile = createTestFile();
            verifyTestFile(testFile);
            destFile = File.createTempFile(TEST_TEMP_FILE_PREFIX, TEST_TEMP_FILE_SUFFIX);
            FileUtils.copyFile(testFile, destFile);
            verifyTestFile(destFile);
        } finally {
            if (testFile != null) testFile.delete();
            if (destFile != null) destFile.delete();
        }
    }
