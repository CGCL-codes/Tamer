    @Override
    protected void setUp() throws Exception {
        super.setUp();
        usersFile = File.createTempFile("Users.", ".txt");
        FileUtils.copyFile(USERS_FILE, usersFile);
        usersFile.deleteOnExit();
        service = new FileUserService();
        service.setDataFile(usersFile);
        service2 = new FileUserService();
        service2.setDataFile(usersFile);
    }
