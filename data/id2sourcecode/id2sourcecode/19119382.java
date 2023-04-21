    @Test
    public void testFileOpenErrors1() throws Exception {
        File file = new File("examples/refsim/file_open/blocked.txt");
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        FileLock lock = fos.getChannel().lock();
        runTest("examples/refsim/file_open", "BuildPathErrors1.txt", 1, 160);
        lock.release();
        fos.close();
        file.delete();
    }
