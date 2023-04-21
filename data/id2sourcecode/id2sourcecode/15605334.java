    private void _ensureNotAlreadyRunning() {
        if (_runningThreads.contains(Thread.currentThread())) {
            throw new DeadlockException("Same thread cannot read or write multiple " + "times!  (Would cause deadlock.)");
        }
    }
