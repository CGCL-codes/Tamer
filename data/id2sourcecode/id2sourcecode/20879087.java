    private void doAccept(SelectionKey acceptKey) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("do accept");
        }
        ServerSocketChannel ssc = (ServerSocketChannel) acceptKey.channel();
        SocketChannel channel = ssc.accept();
        if (channel == null) {
            throw new IOException("accept nothing when do accept key");
        }
        channel.configureBlocking(false);
        log.info("do accept: " + channel);
        SelectionKey taskKey = channel.register(acceptSelector, SelectionKey.OP_READ);
        TaskReader<E, M> reader = new TaskReader<E, M>(createEmpetyTask(), taskKey, getRequestParser(), getResponseParser());
        TaskWriter writer = new TaskWriter(taskKey);
        TaskAttach<E, M> attach = new TaskAttach<E, M>(reader, writer);
        taskKey.attach(attach);
        Socket socket = channel.socket();
        String host = socket.getInetAddress().getHostAddress();
        int port = socket.getPort();
        addKey(host, port, taskKey);
    }
