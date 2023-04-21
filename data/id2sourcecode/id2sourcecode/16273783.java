    public synchronized boolean write_lock_noblock() {
        if ((writer_locks.size() == 0) && (active_readers == 0) && (active_writers == 0)) {
            ++active_writers;
            return true;
        }
        return false;
    }
