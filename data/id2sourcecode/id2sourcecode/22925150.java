    public RemoteInputStream asyncFetch(final int size) throws RemoteException {
        final FastPipedOutputStream pos = new FastPipedOutputStream();
        final FastPipedInputStream pin;
        try {
            pin = new FastPipedInputStream(pos);
        } catch (IOException ioe) {
            throw new IllegalStateException(ioe);
        }
        String curThreadName = Thread.currentThread().getName();
        Thread writeThread = new Thread("asyncFetch#" + curThreadName) {

            public void run() {
                try {
                    fetchTo(size, pos);
                } catch (IOException e) {
                    throw new IllegalStateException("failed to fetch items", e);
                }
            }
        };
        writeThread.start();
        IRemoteInputStreamProxy proxy = new RemoteInputStreamProxy(pin);
        RemoteInputStream remote = new RemoteInputStream(proxy);
        synchronized (writeThread) {
            try {
                writeThread.wait();
            } catch (InterruptedException e) {
            }
        }
        return remote;
    }
