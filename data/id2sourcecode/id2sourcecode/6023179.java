    @Override
    protected void setUp() throws Exception {
        File fixturesDirectory = new File("test/br/ucam/kuabaSubsystem/fixtures/");
        this.fixtures = fixturesDirectory.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()) return false;
                return true;
            }
        });
        testKuabaKnowlegeBase = new File("test/br/ucam/kuabaSubsystem/testBase/testKuabaKnowlegeBase.xml");
        FileUtil.clearFile(testKuabaKnowlegeBase);
        FileUtil.copyFile(new File("test/br/ucam/kuabaSubsystem/fixtures/headers/header.txt"), this.testKuabaKnowlegeBase);
        for (File fixture : this.fixtures) {
            FileUtil.copyFile(fixture, this.testKuabaKnowlegeBase);
        }
        FileUtil.copyFile(new File("test/br/ucam/kuabaSubsystem/fixtures/headers/EOF.txt"), this.testKuabaKnowlegeBase);
        File kuabaRepository = new File("kuabaOntology/");
        System.out.println("Path: " + kuabaRepository.getAbsolutePath());
        assert kuabaRepository.exists();
        this.lr = new LocalFolderRepository(kuabaRepository, true);
        System.out.println("lista vazia? " + lr.getOntologies().isEmpty());
        System.out.println("Cont�m KuabaOntology " + lr.contains(URI.create("http://www.tecweb.inf.puc-rio.br/DesignRationale/KuabaOntology.owl")));
        model = ProtegeOWL.createJenaOWLModel();
        model.getRepositoryManager().addProjectRepository(this.lr);
        System.out.println("existe: " + this.testKuabaKnowlegeBase.exists());
        model.load(new FileInputStream(testKuabaKnowlegeBase), "oi");
        this.factory = new MyFactory(model);
    }
