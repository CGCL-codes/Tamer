    @Test
    public void copyFile_FileValid() throws Exception {
        File fileSrc = new File(Testing.TESTDATA_FOLDER, "readme.txt");
        File fileTgt = Testing.getTempFile(".txt");
        FileUtils.copyFile(fileSrc, fileTgt, false);
        checkSourceAndTargetFileSame(fileSrc, fileTgt);
        assertTrue(true);
    }
