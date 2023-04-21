    @Test
    public void testMoveFileToDirectorySafely() throws IOException {
        File originalFile = new File(testDataFolder, "3805bytes.log");
        FileUtils.copyFileToDirectory(originalFile, testTempFolder);
        File inputFile = new File(testTempFolder, originalFile.getName());
        assertTrue(inputFile.getAbsolutePath() + " not found", inputFile.exists());
        File newDir = new File(testTempFolder, "FileUtilsTest");
        assertTrue(newDir.mkdirs());
        assertTrue(newDir.exists());
        LastFileUtils.moveFileToDirectorySafely(inputFile, newDir, false);
        assertFalse(inputFile.getAbsolutePath() + " exists", inputFile.exists());
        File movedFile = new File(newDir, inputFile.getName());
        assertTrue(movedFile.getAbsolutePath() + " doesn't exist", movedFile.exists());
        assertEquals(FileUtils.readFileToString(originalFile), FileUtils.readFileToString(movedFile));
    }
