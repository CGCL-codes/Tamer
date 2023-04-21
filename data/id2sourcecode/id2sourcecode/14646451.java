    @Test
    public void testMoveFileSafely() throws IOException {
        File originalFile = new File(testDataFolder, "3805bytes.log");
        FileUtils.copyFileToDirectory(originalFile, testTempFolder);
        File inputFile = new File(testTempFolder, originalFile.getName());
        assertTrue(inputFile.getAbsolutePath() + " not found", inputFile.exists());
        File movedFile = new File(testTempFolder, "copy.log");
        LastFileUtils.moveFileSafely(inputFile, movedFile);
        assertFalse(inputFile.getAbsolutePath() + " exists", inputFile.exists());
        assertTrue(movedFile.getAbsolutePath() + " doesn't exist", movedFile.exists());
        assertEquals(FileUtils.readFileToString(originalFile), FileUtils.readFileToString(movedFile));
    }
