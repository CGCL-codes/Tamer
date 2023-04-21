    public void setUp() throws Exception {
        File root = new File(System.getProperty("java.io.tmpdir"), "BrowserTest");
        File subdir = new File(root, "subdir");
        subdir.mkdirs();
        File photo = new File(System.getProperty("project.root"), "build/test/exif-nordf.jpg");
        FileUtils.copyFileToDirectory(photo, root);
        FileUtils.copyFileToDirectory(photo, subdir);
        filesystem = new FileSystemImpl();
        filesystem.setRoot(root);
        PersisterImpl persister = new PersisterImpl();
        persister.setFilesystem(filesystem);
        persister.setTranslator(new Translator());
        browser = new FileSystemBrowser();
        browser.setFilesystem(filesystem);
        browser.setPersister(persister);
    }
