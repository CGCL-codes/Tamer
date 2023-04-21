    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TestEnvironment.reset();
        readAllFiles = new FilePermission("<<ALL FILES>>", "read");
        alsoReadAllFiles = new FilePermission("<<ALL FILES>>", "read");
        allInCurrent = new FilePermission("*", "read, write, execute,delete");
        readInCurrent = new FilePermission("*", "read");
        readInFile = new FilePermission("aFile.file", "read");
    }
