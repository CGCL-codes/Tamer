    public void testMove() throws Exception {
        File currentDir = PXFileUtility.getCurrentDirectory();
        File destinationDir = new File(currentDir, "toMove");
        File testDataDir = new File(currentDir, PXFoundationTestSuite.TEST_DATA_DIR_NAME);
        File originalSourceDir = new File(testDataDir, "toCopy");
        if (destinationDir.exists()) {
            assertTrue(PXFileUtility.delete(destinationDir));
        }
        assertTrue(originalSourceDir.isDirectory());
        boolean copied = PXFileUtility.copy(originalSourceDir, destinationDir, null);
        assertTrue(copied);
        assertTrue(destinationDir.exists());
        File sourceDir = destinationDir;
        destinationDir = new File(currentDir, "moved");
        if (destinationDir.exists()) {
            assertTrue(PXFileUtility.delete(destinationDir));
        }
        boolean moved = PXFileUtility.move(sourceDir, destinationDir, null);
        assertTrue(moved);
        assertTrue(destinationDir.exists());
        assertTrue(!sourceDir.exists());
        List sourcePaths = getAllRelativePathsUnder(originalSourceDir);
        List destinationPaths = getAllRelativePathsUnder(destinationDir);
        assertEquals(sourcePaths, destinationPaths);
    }
