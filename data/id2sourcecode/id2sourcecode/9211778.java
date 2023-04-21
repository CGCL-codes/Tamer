    public synchronized void aquireReadLock() {
        Thread thread = Thread.currentThread();
        while (_writerThread != null && thread != _writerThread) {
            try {
                wait();
            } catch (InterruptedException x) {
            }
        }
        int[] count = _readCounts.get(thread);
        if (count == null) {
            _readCounts.put(thread, new int[] { 1 });
        } else count[0]++;
    }
