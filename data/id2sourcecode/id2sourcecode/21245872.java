    private void init() {
        providerServentsAddress = ProviderServentsAddress.getInstance();
        sendQueue = new SendQueue();
        writerThread = new WriterThread(this);
        readerThread = new ReaderThread(this);
        status = STATUS_DISCONNECTED;
    }
