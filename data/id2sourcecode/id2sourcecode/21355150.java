    @Before
    public void prepare() throws IOException, ClassNotFoundException {
        FileUtils.forceMkdir(new File("./bin/org/docflower/testsuite/enhancer/data/original"));
        simpleClearFolder("./bin/org/docflower/testsuite/enhancer/data/original");
        FileUtils.copyFile(new File("./bin/org/docflower/testsuite/enhancer/data/SampleClassWithList.class"), new File("./bin/org/docflower/testsuite/enhancer/data/original/SampleClassWithList.class"));
        enhance("org/docflower/testsuite/enhancer/data/SampleClassWithList");
    }
