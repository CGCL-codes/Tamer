    public void testLoadError() throws Exception {
        RandomAccessFile ras = null;
        FileLock lock = null;
        try {
            File theFile = new File(INPUT_JAD_PATH);
            ras = new RandomAccessFile(theFile, "rw");
            lock = ras.getChannel().lock();
            executeBuildExceptionTarget("SignTaskTest.testIOError");
            assertExceptionMessageContains("IOException");
        } finally {
            lock.release();
            ras.close();
        }
    }
