    public synchronized void reading() {
        if (active_writers == 0 && writer_locks.size() == 0) ++active_readers; else {
            ++waiting_readers;
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }
