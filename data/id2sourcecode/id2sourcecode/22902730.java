    public ConnectorAdapter(Connector connector, MessageListener<Object> dispatcher, ErrorListener<Object> errorHandler, boolean reliable) {
        super(String.valueOf(connector));
        this.connector = connector;
        this.dispatcher = dispatcher;
        this.errorHandler = errorHandler;
        this.reliable = reliable;
        setDaemon(true);
        outbound = new ArrayBlockingQueue<ByteBuffer>(OUTBOUND_BACKLOG);
        writer = new WriterThread();
        writer.start();
    }
