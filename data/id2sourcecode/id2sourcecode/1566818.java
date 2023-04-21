    protected void setUp() {
        super.setUp();
        project = createFrameworkProject();
        final IModuleLookup moduleLookup = getFrameworkInstance().getModuleLookup();
        final File baseDir = new File(moduleLookup.getBaseDir(), MODULE_NAME);
        baseDir.mkdirs();
        final File commandsDir = new File(baseDir, "commands");
        commandsDir.mkdirs();
        final File libDir = new File(baseDir, "lib");
        libDir.mkdirs();
        Properties moduleProperties = new Properties();
        moduleProperties.put("module.name", MODULE_NAME);
        Properties commandProperties = new Properties();
        final String prefix = "command." + TEST_USER_INPUT_COMMAND;
        commandProperties.put(prefix + ".command-type", "ant");
        commandProperties.put(prefix + ".controller", "TestModule");
        commandProperties.put(prefix + ".doc", "Test user input");
        final File fromFile = new File(TEST_HANDLERS_PATH + "/" + TEST_USER_INPUT_COMMAND + ".xml");
        final File toFile = new File(commandsDir, TEST_USER_INPUT_COMMAND + ".xml");
        final File libxml = new File(TEST_MODULE_LIB_DIR + "/command.xml");
        assertTrue(libxml.exists());
        final File destlibxml = new File(libDir, "command.xml");
        try {
            moduleProperties.store(new FileOutputStream(new File(baseDir, "module.properties")), "Module properties");
            commandProperties.store(new FileOutputStream(new File(baseDir, "commands.properties")), "Command properties");
            FileUtils.copyFileStreams(fromFile, toFile);
            FileUtils.copyFileStreams(libxml, destlibxml);
        } catch (IOException e) {
            fail("caught exception creating module.properties test data: " + e.getMessage());
        }
        assertTrue(toFile.exists());
        assertTrue(destlibxml.exists());
        module = new CmdModule(MODULE_NAME, baseDir, moduleLookup);
    }
