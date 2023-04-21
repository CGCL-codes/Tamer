    @Test
    public void testFindAvailableCopy() throws IOException, PhotovaultException {
        File testDir = new File(System.getProperty("basedir"), "testfiles");
        File testFile = new File(testDir, "test2.jpg");
        File testFileDst = new File(vol2.getBaseDir(), "test2.jpg");
        FileUtils.copyFile(testFile, testFileDst);
        ImageFile imgFile = new ImageFile(testFileDst);
        File f = imgFile.findAvailableCopy();
        assertNull(f);
        imgFile.addLocation(new FileLocation(vol2, "test2.jpg"));
        f = imgFile.findAvailableCopy();
        assertEquals(testFileDst, f);
    }
