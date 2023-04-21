    @Override
    public void open() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.connect(isa);
        Logging.msgln("Info: SocketTraceSource connected");
        FileInputStream inStream = new FileInputStream(traceFile);
        inChannel = inStream.getChannel();
        new Thread("Socket Receiver") {

            public void run() {
                listenAndWrite();
            }
        }.start();
        FeedGroupRegistry.addFeed(feed);
    }
