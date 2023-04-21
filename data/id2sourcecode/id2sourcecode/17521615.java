    public TestExecutor(String testName) throws IOException {
        super(testName);
        if (System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("win")) {
            for (int i = 1; i <= 7; i++) {
                File base = new File(accountsDir, "Test" + i);
                File pwdFile = new File(base, "security" + File.separator + "passwordWin");
                if (pwdFile.exists()) {
                    FileUtils.copyFile(pwdFile, new File(base, "security" + File.separator + "password"));
                    pwdFile.delete();
                }
            }
        }
    }
