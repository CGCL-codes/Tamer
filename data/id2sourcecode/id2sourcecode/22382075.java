    public synchronized void readingFull() {
        if (--active_readers == 0) notify_writers();
    }
