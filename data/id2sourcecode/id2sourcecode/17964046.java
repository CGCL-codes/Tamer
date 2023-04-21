    public void lock() throws InterruptedException, IOException {
        if (this.lock != null) return;
        try {
            sem.acquire();
            while (lock == null) {
                if (file.exists()) {
                    boolean deleted = file.delete();
                    if (deleted) {
                        this.raf = new RandomAccessFile(file, "rw");
                        this.lock = raf.getChannel().lock();
                    }
                }
                Thread.sleep(1000);
            }
        } finally {
            if (lock == null) sem.release();
        }
    }
