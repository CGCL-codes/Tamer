    public void handleAcceptable(SelectionKey selKey) throws IOException {
        ServerSocketChannel socketChannel = (ServerSocketChannel) selKey.channel();
        ServerSocket serverSocket = socketChannel.socket();
        try {
            Socket socket = serverSocket.accept();
            socket.getChannel().configureBlocking(false);
            RunningSlave slave = new RunningSlave();
            slave.setIp(socket.getInetAddress().getHostAddress());
            slave.setPort(socket.getPort());
            slave.setSocket(socket);
            RunningSlavesRegistry.registerRunningSlave(slave);
            socket.getChannel().register(_selector, SelectionKey.OP_READ & SelectionKey.OP_WRITE);
        } catch (ClosedChannelException e) {
            e.printStackTrace();
        }
    }
