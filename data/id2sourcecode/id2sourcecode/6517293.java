            public void run() {
                try {
                    setThreadName("writer-thread");
                    writeThread();
                } finally {
                    latch.countDown();
                }
            }
