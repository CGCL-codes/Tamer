    public void testCopyFileProgressReporter() throws IOException {
        File testFile = null;
        File destFile = null;
        try {
            testFile = createTestFile();
            verifyTestFile(testFile);
            destFile = File.createTempFile(TEST_TEMP_FILE_PREFIX, TEST_TEMP_FILE_SUFFIX);
            DefaultProgressReporter reporter = new DefaultProgressReporter();
            FileUtils.copyFile(testFile, destFile, reporter);
            verifyTestFile(destFile);
            assertFalse(reporter.isCanceled());
        } finally {
            if (testFile != null) testFile.delete();
            if (destFile != null) destFile.delete();
        }
    }
