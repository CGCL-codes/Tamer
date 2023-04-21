    @Test(expected = NullPointerException.class)
    public void testMoveFileToDirectorySafely_NullDir_CreateDir() throws IOException {
        File originalFile = new File(testDataFolder, "3805bytes.log");
        FileUtils.copyFileToDirectory(originalFile, testTempFolder);
        File inputFile = new File(testTempFolder, originalFile.getName());
        assertTrue(inputFile.getAbsolutePath() + " not found", inputFile.exists());
        LastFileUtils.moveFileToDirectorySafely(inputFile, null, true);
    }
