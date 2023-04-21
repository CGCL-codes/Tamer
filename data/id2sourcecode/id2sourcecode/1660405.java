    protected MsnServer(String host, int port, ProxyInfo info) throws UnknownHostException, IOException, IllegalStateException {
        conn = info.getConnection(host, port);
        nextTransactionID = new Integer(1);
        callbackMap = new Hashtable();
        syncCallID = new Integer(0);
        readBuffer = new Vector();
        writeBuffer = new Vector();
        reader = new ReaderThread(this, conn, readBuffer);
        writer = new WriterThread(this, conn, writeBuffer);
        reader.start();
        writer.start();
        this.start();
    }
