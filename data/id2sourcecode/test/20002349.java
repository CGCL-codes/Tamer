    protected void awaitLoad() throws WaveletStateException {
        Preconditions.checkState(!writeLock.isHeldByCurrentThread(), "should not hold write lock");
        try {
            if (!loadLatch.await(AWAIT_LOAD_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                throw new WaveletStateException("Timed out waiting for wavelet to load");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WaveletStateException("Interrupted waiting for wavelet to load");
        }
    }
