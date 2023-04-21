    protected void backupJSystemProperties() throws Exception {
        userDir = applicationClient.getUserDir();
        File orig = new File(userDir, "jsystem.properties");
        File back = new File(orig.getParentFile(), "jsystem.properties.back");
        FileUtils.copyFile(orig, back);
    }
