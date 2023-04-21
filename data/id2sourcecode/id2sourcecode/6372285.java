    public void testCopyFile() {
        File[] srcFiles = this.srcDirFile.listFiles();
        srcFiles[0].delete();
        IOException e = null;
        try {
            FileUtils.copyFile(srcFiles[0], new File(this.tgtDirFile, srcFiles[0].getName()));
        } catch (IOException ioe) {
            e = ioe;
        }
        assertNotNull("Didn't get expected IOE", e);
    }
