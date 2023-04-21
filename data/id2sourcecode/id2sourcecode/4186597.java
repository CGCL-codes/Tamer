    public void testDeadlock() {
        int threads = 20;
        int resources = 200;
        try {
            Thread[] writerThreads = new Thread[threads];
            for (int i = 0; i < threads; i++) {
                writerThreads[i] = new WriterThread(rootCollection, resources);
                writerThreads[i].setName("T" + i);
                writerThreads[i].start();
            }
            for (int i = 0; i < threads; i++) {
                writerThreads[i].join();
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
