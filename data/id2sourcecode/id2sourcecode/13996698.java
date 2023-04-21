    public void testContinuously() {
        PerThreadFixture threadFixture = threadFixtures.get();
        threadFixture.timingController = getTimingController().getControllerForCurrentThread();
        Thread readerThread = new Thread(new Reader(Thread.currentThread(), threadFixture));
        readerThread.start();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                threadFixture.writerThrottle.throttle();
                threadFixture.buffer.put(System.nanoTime());
            } catch (InterruptedException e) {
                e = null;
                if (threadFixture.shouldStop) {
                    break;
                } else {
                    Thread.currentThread().interrupt();
                    fail("Writer thread was unexpectedly interrtuped.");
                }
            }
        }
    }
