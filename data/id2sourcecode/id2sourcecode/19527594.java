    public void acquireWriterLock() throws InterruptedException {
        synchronized (_monitor) {
            waitForWakingUp();
            if (_readerCount > 0 || _writerLock) {
                _waitingWriters++;
                try {
                    do {
                        _monitor.wait();
                        if (_wakeUpWriters == 1) {
                            _wakeUpWriters = 0;
                            _monitor.notifyAll();
                            break;
                        }
                    } while (true);
                } finally {
                    _waitingWriters--;
                }
            }
            _writerLock = true;
        }
    }
