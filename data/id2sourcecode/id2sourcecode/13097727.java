    private File getTestFile() {
        final URL url = TestCrueLOG.class.getResource(FICHIER_TEST_XML);
        final File otfaFile = new File(createTempDir(), "logs.log.xml");
        try {
            CtuluLibFile.copyStream(url.openStream(), new FileOutputStream(otfaFile), true, true);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        return otfaFile;
    }
