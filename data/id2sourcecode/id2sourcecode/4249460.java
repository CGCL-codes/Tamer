    public void startup() throws XMPPException {
        writerThread.start();
        Semaphore connectionSemaphore = new Semaphore(1);
        readerThread.start();
        try {
            connectionSemaphore.acquire();
            int waitTime = SmackConfiguration.getPacketReplyTimeout();
            connectionSemaphore.tryAcquire(3 * waitTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException ie) {
        }
    }
