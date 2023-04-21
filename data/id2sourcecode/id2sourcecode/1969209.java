    @Test
    public void testCopyFileToDirectoryFileFile() throws DirectoryAllreadyExistsException, FileIsNotADirectoryException, IOException, FileIsADirectoryException {
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
        this.result = CopyFileUtils.copyFileToDirectory(srcFile, srcDir);
        final File expectedCopiedFile = new File(srcDir, filePrefix + txtSuffix);
        assertTrue("File " + expectedCopiedFile.getAbsolutePath() + " should be copied.", expectedCopiedFile.exists());
        assertTrue("long lastModified is not the same.", srcFile.lastModified() == expectedCopiedFile.lastModified());
    }
