    public void testCopyFiles() throws IOException {
        FileUtils.copyFiles(this.srcDirFile, this.tgtDirFile);
        File[] srcFiles = this.srcDirFile.listFiles();
        for (int i = 0; i < srcFiles.length; i++) {
            File tgt = new File(this.tgtDirFile, srcFiles[i].getName());
            assertTrue("Tgt doesn't exist " + tgt.getAbsolutePath(), tgt.exists());
        }
    }
