    public synchronized void endRead() {
        if (_numActiveReaders == 0) {
            throw new IllegalStateException("Trying to end a read with no active readers!");
        }
        _numActiveReaders--;
        _ensureAlreadyRunning();
        _runningThreads.remove(Thread.currentThread());
        if (_numActiveWriters > 0) {
            String msg = "A writer was active during a read!";
            throw new UnexpectedException(new Exception(msg));
        }
        if (_numActiveReaders == 0) {
            _wakeFrontGroupOfWaitQueue();
        }
    }
