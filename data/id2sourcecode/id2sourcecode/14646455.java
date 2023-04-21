    @Test
    public void testMoveFileToDirectorySafely_CreateDir() throws IOException {
        File originalFile = new File(testDataFolder, "3805bytes.log");
        FileUtils.copyFileToDirectory(originalFile, testTempFolder);
        File inputFile = new File(testTempFolder, originalFile.getName());
        assertTrue(inputFile.getAbsolutePath() + " not found", inputFile.exists());
        File newDir = new File(testTempFolder, "FileUtilsTest");
        assertFalse(newDir.exists());
        LastFileUtils.moveFileToDirectorySafely(inputFile, newDir, true);
        assertFalse(inputFile.getAbsolutePath() + " exists", inputFile.exists());
        File movedFile = new File(newDir, inputFile.getName());
        assertTrue(movedFile.getAbsolutePath() + " doesn't exist", movedFile.exists());
        assertEquals(FileUtils.readFileToString(originalFile), FileUtils.readFileToString(movedFile));
    }
