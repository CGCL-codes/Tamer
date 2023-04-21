    protected void restoreJSystemProperties() throws Exception {
        if (userDir == null) {
            return;
        }
        File orig = new File(userDir, "jsystem.properties");
        File back = new File(orig.getParentFile(), "jsystem.properties.back");
        FileUtils.copyFile(back, orig);
    }
