    public void testConcurrentWrites2() throws IOException {
        Directory dir = getTestResourceLocation().getDirectory(TestResources.JAVA_TEST_DIR);
        File f = new File(dir.getAbsolutePath() + "\\test-file.tmp");
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
        FileOutputStream out1 = new FileOutputStream(f, true);
        out1.write("abc".getBytes());
        FileOutputStream out2 = new FileOutputStream(f, true);
        FileChannel fc = out2.getChannel();
        FileLock lock = fc.lock();
        if (lock == null) {
            throw new RuntimeException();
        }
        out1.write("def".getBytes());
        out1.close();
        out2.close();
    }
