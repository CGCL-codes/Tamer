    protected void implCloseSelectableChannel() throws IOException {
        synchronized (stateLock) {
            nd.preClose(fd);
            if (registry != null) registry.invalidateAll();
            long th;
            if ((th = readerThread) != 0) NativeThread.signal(th);
            if ((th = writerThread) != 0) NativeThread.signal(th);
            if (!isRegistered()) kill();
        }
    }
