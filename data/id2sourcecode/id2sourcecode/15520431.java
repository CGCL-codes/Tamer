    public void testReadWriteWithTimeout() {
        final AtomicBoolean result = new AtomicBoolean(true);
        class Writer implements Runnable {

            private TestEntry entry = new TestEntry(FIRST);

            private int count = QUEUE_ELEMENTS;

            public void run() {
                do {
                    try {
                        queue.offer(entry, 10, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count--;
                } while (count > 0);
            }
        }
        class Reader implements Runnable {

            private TestEntry entry = null;

            private int count = QUEUE_ELEMENTS;

            public void run() {
                do {
                    try {
                        entry = queue.poll(10, TimeUnit.SECONDS);
                        if (entry == null) {
                            result.set(false);
                        }
                        if (!entry.getEntry().equals(FIRST)) {
                            result.set(false);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    count--;
                } while (count > 0);
            }
        }
        Thread writerThread = new Thread(new Writer());
        Thread readerThread = new Thread(new Reader());
        writerThread.start();
        readerThread.start();
        try {
            readerThread.join();
            writerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!queue.isEmpty()) {
            result.set(false);
        }
        assertTrue(result.get());
    }
