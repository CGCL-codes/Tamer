    public void testCopy() throws Exception {
        File currentDir = PXFileUtility.getCurrentDirectory();
        File destinationDir = new File(currentDir, "copied");
        File testDataDir = new File(currentDir, PXFoundationTestSuite.TEST_DATA_DIR_NAME);
        File sourceDir = new File(testDataDir, "toCopy");
        if (destinationDir.exists()) {
            assertTrue(PXFileUtility.delete(destinationDir));
        }
        assertTrue(sourceDir.isDirectory());
        boolean copied = PXFileUtility.copy(sourceDir, destinationDir, null);
        assertTrue(copied);
        List sourcePaths = getAllRelativePathsUnder(sourceDir);
        List destinationPaths = getAllRelativePathsUnder(destinationDir);
        assertEquals(sourcePaths, destinationPaths);
    }
