    @Test
    public void moveFile() throws Exception {
        File folderSrc = Testing.getTempFolder("src");
        File srcFile = new File(folderSrc, "temp.xml");
        File folderTgt = Testing.getTempFolder("tgt");
        File tgtFile = new File(folderTgt, "temp.xml");
        FileUtils.copyFile(new File(Testing.TESTDATA_FOLDER, "readme.txt"), srcFile, false);
        assertTrue("Test Source File should exist", srcFile.exists());
        FileUtils.moveFile(srcFile, tgtFile);
        assertFalse("Source File should not exist", srcFile.exists());
        assertTrue("Target File should exist", tgtFile.exists());
    }
