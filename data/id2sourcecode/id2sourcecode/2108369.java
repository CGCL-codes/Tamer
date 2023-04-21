    protected void setUp() throws Exception {
        File sub = getBaseIndexDir();
        sub.mkdir();
        File[] files = sub.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                FileHelper.delete(file);
            }
        }
        super.setUp();
    }
