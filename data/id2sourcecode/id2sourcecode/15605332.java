    public synchronized void endWrite() {
        if (_numActiveWriters != 1) {
            throw new IllegalStateException("Trying to end a write with " + _numActiveWriters + " active writers!");
        }
        _numActiveWriters--;
        _ensureAlreadyRunning();
        _runningThreads.remove(Thread.currentThread());
        if ((_numActiveWriters > 0) || (_numActiveReaders > 0)) {
            String msg = "Multiple readers/writers were active during a write!";
            throw new UnexpectedException(new Exception(msg));
        }
        _wakeFrontGroupOfWaitQueue();
    }
