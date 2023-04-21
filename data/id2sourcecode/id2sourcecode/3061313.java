    public void testCopySimple() throws Exception {
        makeTempDir();
        File source = new File(TEMP_DIR_NAME + "/sourceFile");
        File dest = new File(TEMP_DIR_NAME + "/dest");
        createFile(source);
        try {
            FileUtils.copyFile(source, dest, false);
        } catch (Exception e) {
            assert (false);
        }
        assert (dest.exists());
        cleanUpTempDir();
    }
