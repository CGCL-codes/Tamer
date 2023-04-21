    public void testLockFile() throws Exception {
        File xls = new File(getTestResourceLocation().getDirectory(TestResources.JAVA_TEST_DIR).getAbsolutePath() + "/" + "lockedFile.xls");
        FileOutputStream out = new FileOutputStream(xls);
        FileChannel channel = out.getChannel();
        FileLock lock = channel.tryLock();
        if (lock == null) {
            println("it was not possible to lock the file.");
            return;
        }
        try {
            println("press ENTER to continue...");
            System.in.read();
        } finally {
            lock.release();
        }
        out.close();
    }
