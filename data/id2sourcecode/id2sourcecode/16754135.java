    private static void changeTestFile(String filePath) {
        try {
            FileUtils.copyFile(new File(filePath), configFile);
        } catch (IOException e) {
            Assert.fail("Failed to copy the test file to " + configFile + " " + e.getMessage());
        }
    }
