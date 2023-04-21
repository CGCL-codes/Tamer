    public void threadSetUp() {
        PerThreadFixture threadFixture = new PerThreadFixture();
        threadFixture.buffer = new LinkedBlockingQueue<Long>(BUFFER_SIZE);
        threadFixture.writerThrottle = new SleepThrottle();
        threadFixture.writerThrottle.setRate(ARRIVAL_RATE);
        threadFixture.readerThrottle = new SleepThrottle();
        threadFixture.readerThrottle.setRate(PROCESSING_RATE);
        threadFixture.shouldStop = false;
        threadFixtures.set(threadFixture);
    }
