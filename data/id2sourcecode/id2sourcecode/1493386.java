    synchronized boolean ownWriteLock(final Thread _thread) {
        return writer_ == _thread && writeLocks_ > 0;
    }
