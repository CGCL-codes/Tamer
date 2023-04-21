    private static void confirmFileExists() {
        if (!configFile.exists()) {
            try {
                FileUtils.copyFile(new File(getOriginalFilePath()), configFile);
            } catch (IOException e) {
                Assert.fail("Failed to copy file from original path to " + configFile + " " + e.getMessage());
            }
        }
    }
