    synchronized void scheduleFlush() {
        if (flusher == null) {
            flusher = new Thread() {

                public void run() {
                    try {
                        Thread.sleep(writeLatency);
                        synchronized (DiskStore.this) {
                            if (references <= 0) return;
                        }
                        flush(true);
                    } catch (InterruptedException e) {
                    }
                }
            };
            flusher.start();
        }
    }
