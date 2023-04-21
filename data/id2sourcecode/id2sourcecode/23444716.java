    private void copyDummyPropertiesFileToUserHome() throws IOException {
        String userHome = System.getProperty("user.home");
        String classPath = this.getClass().getClassLoader().getResource(".").getPath();
        File fileToCopy = new File(classPath + "/" + TEST_FILE);
        FileUtils.copyFileToDirectory(fileToCopy, new File(userHome));
        File copiedFile = new File(userHome + "/" + TEST_FILE);
        assertTrue("File " + TEST_FILE + " should be in user home.", copiedFile.exists());
    }
