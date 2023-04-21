    @Override
    protected Serializable doTask() throws IOException {
        interruptThread.start();
        final ServerSocketChannel channel = ServerSocketChannel.open();
        channel.socket().bind(new InetSocketAddress(port));
        final ServerSocket serverSocket = channel.socket();
        while (!stopped) {
            FileServerRunnable runnable = new FileServerRunnable(serverSocket.getChannel().accept().socket());
            runnable.addPropertyChangeListener(this);
            new Thread(runnable).start();
        }
        return null;
    }
