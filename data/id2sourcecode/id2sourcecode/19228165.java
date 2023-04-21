    public final void register(Rmap rmapI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.EOFException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        try {
            readLock.setIdentification(getFullName() + "_handle_read");
            writeLock.setIdentification(getFullName() + "_handle_write");
            synchronized (this) {
                while (readLock.isLocked() || writeLock.isLocked()) {
                    wait(TimerPeriod.NORMAL_WAIT);
                }
                readLock.lock("PlugInHandle.register");
                writeLock.lock("PlugInHandle.register");
            }
            getACO().register(rmapI);
        } finally {
            writeLock.unlock();
            readLock.unlock();
            synchronized (this) {
                notify();
            }
        }
    }
