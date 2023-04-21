    private static boolean lock() {
        try {
            final FileLock lock = new FileOutputStream(new File(rootDir, LOCK_FILE_NAME)).getChannel().tryLock();
            if (lock != null) {
                Thread t = new Thread(new Runnable() {

                    public void run() {
                        while (true) {
                            try {
                                if (lock.isValid()) {
                                }
                                ;
                                Thread.sleep(Long.MAX_VALUE);
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                });
                t.setDaemon(true);
                t.start();
            }
            return lock != null;
        } catch (Exception ex) {
        }
        return true;
    }
