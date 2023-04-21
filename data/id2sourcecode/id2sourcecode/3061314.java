    public void testCopyExceptions() throws Exception {
        cleanUpTempDir();
        File source = new File(TEMP_DIR_NAME + "/sourceFile");
        File dest = new File(TEMP_DIR_NAME + "/dest");
        try {
            FileUtils.copyFile(source, dest, false);
            assert (false);
        } catch (DirNotFoundException dnfe) {
        } catch (Exception e) {
            e.printStackTrace();
            assert (false);
        }
        makeTempDir();
        try {
            FileUtils.copyFile(source, dest, false);
            assert (false);
        } catch (FileNotFoundException fnfe) {
        } catch (Exception e) {
            assert (false);
        }
        createFile(source);
        createFile(dest);
        try {
            FileUtils.copyFile(source, dest, false);
            assert (false);
        } catch (FileExistsAlreadyException feae) {
        } catch (Exception e) {
            assert (false);
        }
        assert (dest.exists());
        cleanUpTempDir();
    }
