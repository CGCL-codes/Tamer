    private void lockImpl() throws IOException {
        if (lock != null) return;
        if (lockFile.exists()) lockFile.delete();
        channel = new RandomAccessFile(lockFile, "rw").getChannel();
        lock = channel.tryLock();
        if (lock == null) {
            channel.close();
            Validator.STATE.fail("Lock file held by another process: " + lockFile);
        }
        if (!hookedToShutdown) {
            Runtime.getRuntime().addShutdownHook(new Thread() {

                public void run() {
                    release();
                }
            });
            hookedToShutdown = true;
        }
    }
