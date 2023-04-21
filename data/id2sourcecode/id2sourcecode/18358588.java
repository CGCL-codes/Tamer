    protected void backupJSystemProperties() throws Exception {
        userDir = jsystem.getUserDir();
        File orig = new File(userDir, CommonResources.JSYSTEM_PROPERTIES_FILE_NAME);
        File back = new File(orig.getParentFile(), CommonResources.JSYSTEM_PROPERTIES_FILE_NAME + ".back");
        report.report("Backing up " + orig.getAbsolutePath() + " To " + back.getAbsolutePath());
        FileUtils.copyFile(orig, back);
    }
