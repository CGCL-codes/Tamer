    public void setUp() {
        File testfile1 = null;
        File testfile2 = null;
        File testfile3 = null;
        try {
            extVolDir = File.createTempFile("pv_indexer_test_", "");
            extVolDir.delete();
            extVolDir.mkdir();
            File extVolSubdir = new File(extVolDir, "test");
            extVolSubdir.mkdir();
            testfile1 = new File("testfiles", "test1.jpg");
            testfile2 = new File("testfiles", "test2.jpg");
            testfile3 = new File("testfiles", "test3.jpg");
            photo1 = new File(extVolDir, "test1.jpg");
            FileUtils.copyFile(testfile1, photo1);
            photo2inst1 = new File(extVolDir, "test2.jpg");
            FileUtils.copyFile(testfile2, photo2inst1);
            photo2inst2 = new File(extVolSubdir, "test2.jpg");
            FileUtils.copyFile(testfile2, photo2inst2);
            File txtFile = new File(extVolDir, "test.txt");
            FileWriter writer = new FileWriter(txtFile);
            writer.write("Not an image");
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        topFolder = PhotoFolder.create("ExtVolTest", PhotoFolder.getRoot());
        hash1 = ImageInstance.calcHash(testfile1);
        PhotoInfo photos1[] = PhotoInfo.retrieveByOrigHash(hash1);
        if (photos1 != null) {
            for (int n = 0; n < photos1.length; n++) {
                photos1[n].delete();
            }
        }
        hash2 = ImageInstance.calcHash(testfile2);
        PhotoInfo photos2[] = PhotoInfo.retrieveByOrigHash(hash2);
        if (photos2 != null) {
            for (int n = 0; n < photos2.length; n++) {
                photos2[n].delete();
            }
        }
        hash3 = ImageInstance.calcHash(testfile3);
        PhotoInfo photos3[] = PhotoInfo.retrieveByOrigHash(hash3);
        if (photos3 != null) {
            for (int n = 0; n < photos3.length; n++) {
                photos3[n].delete();
            }
        }
    }
