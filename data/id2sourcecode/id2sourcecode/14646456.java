    @Test(expected = FileNotFoundException.class)
    public void testMoveFileToDirectorySafely_NoCreateDir() throws IOException {
        File originalFile = new File(testDataFolder, "3805bytes.log");
        FileUtils.copyFileToDirectory(originalFile, testTempFolder);
        File inputFile = new File(testTempFolder, originalFile.getName());
        assertTrue(inputFile.getAbsolutePath() + " not found", inputFile.exists());
        File newDir = new File(testTempFolder, "FileUtilsTest");
        assertFalse(newDir.exists());
        LastFileUtils.moveFileToDirectorySafely(inputFile, newDir, false);
    }
