    public void testCopyOverwrite() throws Exception {
        makeTempDir();
        File source = new File(TEMP_DIR_NAME + "/sourceFile");
        File dest = new File(TEMP_DIR_NAME + "/dest");
        createFile(source);
        createFile(dest);
        try {
            FileUtils.copyFile(source, dest, true);
        } catch (Exception e) {
            assert (false);
        }
        cleanUpTempDir();
    }
