    public void testReadWriteWithBlocks() {
        final AtomicBoolean result = new AtomicBoolean(true);
        class Writer implements Runnable {

            private TestEntry entry = new TestEntry(FIRST);

            private int count = QUEUE_ELEMENTS;

            public void run() {
                do {
                    try {
                        queue.put(entry);
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
                        entry = queue.take();
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
