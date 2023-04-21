    public void write_lock() {
        Object lock = new Object();
        synchronized (lock) {
            synchronized (this) {
                boolean okay_to_write = (writer_locks.size() == 0) && (active_readers == 0) && (active_writers == 0);
                if (okay_to_write) {
                    ++active_writers;
                    return;
                }
                writer_locks.addLast(lock);
            }
            try {
                lock.wait();
            } catch (InterruptedException e) {
            }
        }
    }
