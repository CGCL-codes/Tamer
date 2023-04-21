    @SuppressWarnings("CallToThreadDumpStack")
    public TestExecutorFile(String testName) throws IOException {
        super(testName);
        if (System.getProperty("os.name").toLowerCase(Locale.ENGLISH).contains("win")) {
            for (int i = 1; i <= 9; i++) {
                File base = new File(mavenTargetTestClassesDir, "Test" + i);
                File pwdFile = new File(base, "security" + File.separator + "passwordWin");
                if (pwdFile.exists()) {
                    FileUtils.copyFile(pwdFile, new File(base, "security" + File.separator + "password"));
                    pwdFile.delete();
                }
            }
        }
        getBaseCase1Sources();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(mavenTargetTestClassesDir, "users"));
            rcptInfo.load(fis);
            String sender = null;
            Locale locale = Locale.ENGLISH;
            Iterator iter = rcptInfo.keySet().iterator();
            while (iter.hasNext()) {
                sender = (String) iter.next();
                if (sender.toLowerCase(locale).startsWith("sender")) {
                    break;
                }
                sender = null;
            }
            if (sender == null) {
                throw new Exception("You have to specify the sender by including an entry in the users file that starts with \"sender.\"");
            }
            senderCredentials = new PasswordAuthenticator(sender.substring(7), rcptInfo.getProperty(sender));
            rcptInfo.remove(sender);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(999);
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }
