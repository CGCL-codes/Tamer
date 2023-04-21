    protected void implCloseSelectableChannel() throws IOException {
        synchronized (stateLock) {
            isInputOpen = false;
            isOutputOpen = false;
            nd.preClose(fd);
            if (readerThread != 0) NativeThread.signal(readerThread);
            if (writerThread != 0) NativeThread.signal(writerThread);
            if (!isRegistered()) kill();
        }
    }
