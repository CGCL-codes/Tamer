    protected synchronized void readInit(SelectSourceIF read_selsource, ISink compQ, int readClogTries) {
        if (DEBUG) System.err.println("readInit called on " + this);
        if (closed) return;
        this.read_selsource = (NIOSelectSource) read_selsource;
        this.read_selsource.setName("ReadSelectSource");
        this.readCompQ = compQ;
        this.readClogTries = readClogTries;
        if (DEBUG) System.err.println("n_keys = " + ((NIOSelectSource) read_selsource).getSelector().keys().size());
        rselkey = (SelectionKey) read_selsource.register(nbsock.getChannel(), SelectionKey.OP_READ);
        if (rselkey == null) {
            System.err.println("SockState: register returned null");
            return;
        }
        rselkey.attach(this);
    }
