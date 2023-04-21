    public void startup() throws Exception {
        FileUtils.deleteDirectory(testDir);
        if (testFile != null) {
            if (testFile.isDirectory()) {
                FileUtils.copyDirectory(testFile, testDir);
            } else {
                FileUtils.copyFileToDirectory(testFile, testDir);
            }
        } else {
            testDir.mkdirs();
        }
        Session session = Session.getInstance(sessionProps);
        store = session.getStore(new URLName("mstor:" + testDir.getAbsolutePath()));
    }
