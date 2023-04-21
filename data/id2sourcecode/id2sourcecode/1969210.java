    @Test
    public void testCopyFileToDirectoryFileFileBoolean() throws FileIsNotADirectoryException, IOException, FileIsADirectoryException, DirectoryAllreadyExistsException {
        String dirToCopyName = "dirToCopy";
        final File srcDir = new File(this.deepDir, dirToCopyName);
        final String filePrefix = "testCopyFile";
        final String txtSuffix = ".txt";
        File srcFile = new File(this.testDir, filePrefix + txtSuffix);
        WriteFileUtils.string2File(srcFile, "Its a beautifull day!!!");
        if (!srcDir.exists()) {
            boolean created = CreateFileUtils.createDirectory(srcDir);
            assertTrue("The directory " + srcDir.getAbsolutePath() + " should be created.", created);
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        this.result = CopyFileUtils.copyFileToDirectory(srcFile, srcDir, false);
        final File expectedCopiedFile = new File(srcDir, filePrefix + txtSuffix);
        assertTrue("File " + expectedCopiedFile.getAbsolutePath() + " should be copied.", expectedCopiedFile.exists());
        assertTrue("long lastModified was set.", srcFile.lastModified() != expectedCopiedFile.lastModified());
    }
