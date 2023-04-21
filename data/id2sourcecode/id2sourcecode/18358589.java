    protected void restoreJSystemProperties() throws Exception {
        if (userDir == null) {
            return;
        }
        File orig = new File(userDir, CommonResources.JSYSTEM_PROPERTIES_FILE_NAME);
        File back = new File(orig.getParentFile(), CommonResources.JSYSTEM_PROPERTIES_FILE_NAME + ".back");
        report.report("Restoring from " + back.getAbsolutePath() + " To " + orig.getAbsolutePath());
        FileUtils.copyFile(back, orig);
    }
