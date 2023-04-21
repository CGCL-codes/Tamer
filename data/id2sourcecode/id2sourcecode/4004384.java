    public void testSaveMeta() throws Exception {
        PersisterImpl persister = new PersisterImpl();
        persister.setStoringInJpeg(false);
        Translator translator = new Translator();
        persister.setTranslator(translator);
        FileSystem filesystem = new FileSystemImpl();
        filesystem.setRoot(new File(System.getProperty("project.root"), "build/test/"));
        persister.setFilesystem(filesystem);
        Date before = new Date();
        File exifJpeg = new File(System.getProperty("project.root"), "build/test/exif-nordf.jpg");
        File rdfJpeg = new File(System.getProperty("project.root"), "build/test/exif-rdf.jpg");
        File destJpeg = new File(System.getProperty("java.io.tmpdir"), "PersisterTest.jpg");
        FileUtils.copyFile(rdfJpeg, destJpeg);
        Metadata exif = persister.getExif(exifJpeg);
        exif = persister.getExif(exifJpeg);
        PhotoMeta exifPhoto = translator.fromExif(exif);
        persister.saveMeta(destJpeg, exifPhoto);
        persister.saveMeta(destJpeg, exifPhoto);
        Model rdf = persister.getRdf(destJpeg);
        PhotoMeta rdfPhoto = (PhotoMeta) translator.fromRdf(rdf);
        assertEquals(exifPhoto.getUpdated(), rdfPhoto.getUpdated());
        assertEquals(exifPhoto, rdfPhoto);
        assertTrue(before.before(exifPhoto.getUpdated()));
        assertTrue(before.before(rdfPhoto.getUpdated()));
    }
