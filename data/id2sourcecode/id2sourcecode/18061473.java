    @Test
    public void testFindByLocation() throws IOException, PhotovaultException {
        File testDir = new File(System.getProperty("basedir"), "testfiles");
        File testFileOrig = new File(testDir, "test2.jpg");
        File testFile2Orig = new File(testDir, "test3.jpg");
        File testFile = new File(vol2.getBaseDir(), "test2.jpg");
        FileUtils.copyFile(testFileOrig, testFile);
        File testFile2 = new File(vol2.getBaseDir(), "test3.jpg");
        FileUtils.copyFile(testFile2Orig, testFile2);
        ImageFile imgFile = new ImageFile(testFile);
        imgFile.addLocation(new FileLocation(vol2, "test2.jpg"));
        ImageFile imgFile2 = new ImageFile(testFile2);
        imgFile2.addLocation(new FileLocation(vol2, "test3.jpg"));
        ifDAO.makePersistent(imgFile);
        ifDAO.makePersistent(imgFile2);
        session.flush();
        assertEquals(imgFile, ifDAO.findFileInLocation((ExternalVolume) vol2, "test2.jpg"));
        assertEquals(imgFile2, ifDAO.findFileInLocation((ExternalVolume) vol2, "test3.jpg"));
        assertNull(ifDAO.findFileInLocation((ExternalVolume) vol2, "test4.jpg"));
    }
