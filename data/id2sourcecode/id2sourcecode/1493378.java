    public synchronized void lockRead() throws IOException {
        if (!canRead()) {
            while (writeLocks_ > 0 || writer_ != null) {
                try {
                    wait();
                } catch (final InterruptedException e) {
                    throw (IOException) new IOException().initCause(e);
                }
            }
        }
        assertTrue("A write lock exists that is owned by another thread", canRead());
        final Thread current = Thread.currentThread();
        Owner owner = (Owner) owners_.get(current);
        if (owner != null) {
            owner.timesLocked_++;
        } else {
            owner = new Owner(current);
            owners_.put(current, owner);
        }
        FuLog.trace("CSG : Start Read Lock:" + owner);
    }
