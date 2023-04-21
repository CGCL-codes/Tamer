    protected void convertAatFile(IFile file, IFolder outFolder, String outFileExtension) throws IOException, CoreException {
        Test test = TestPersistance.loadFromFile(file);
        IFolder destinationFolder = outFolder;
        if (outFolder.getName().indexOf(".aat") == -1) {
            destinationFolder = outFolder.getFolder(file.getName());
        }
        if (!destinationFolder.exists()) {
            destinationFolder.create(false, true, null);
        }
        testConverter.convert(test, destinationFolder.getRawLocation().toFile(), null);
        try {
            IFile destFile = destinationFolder.getFile("default.css");
            if (!destFile.exists()) {
                URL cssUrl = this.getClass().getResource("default.css");
                File cssFile = FileUtils.toFile(FileLocator.toFileURL(cssUrl));
                File destination = destFile.getRawLocation().toFile();
                FileUtils.copyFile(cssFile, destination);
            }
            destFile = destinationFolder.getFile("cubic.js");
            if (!destFile.exists()) {
                URL jsUrl = this.getClass().getResource("cubic.js");
                File jsFile = FileUtils.toFile(FileLocator.toFileURL(jsUrl));
                File destination = destFile.getRawLocation().toFile();
                FileUtils.copyFile(jsFile, destination);
            }
        } catch (Exception e) {
            System.out.println("Error copying stylesheet! Check that resources dir is in source path.");
        }
    }
