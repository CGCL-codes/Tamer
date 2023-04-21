    public synchronized void acquireWriteLock() {
        Thread thread = Thread.currentThread();
        while (true) {
            if (_writerThread == thread) break;
            if (_readCounts.isEmpty()) break;
            if (_readCounts.size() == 1 && _readCounts.containsKey(thread)) break;
            try {
                wait();
            } catch (InterruptedException x) {
            }
        }
        if (_writerThread == null) {
            _writerThread = thread;
            _writeCount = 1;
        } else _writeCount++;
    }
