    protected void doCloseAll(String parent) throws IOException {
        Map<SocketAddress, Connection> connectionMap = null;
        SocketConnection connection = null;
        SocketChannel socketChannel = null;
        SelectionKey selectionKey = null;
        SocketAddress socketAddress = null;
        connectionMap = container.getConnectionMap();
        for (Map.Entry<SocketAddress, Connection> entry : connectionMap.entrySet()) {
            socketAddress = entry.getKey();
            connection = (SocketConnection) entry.getValue();
            if (connection.getParent().compareTo(parent) == 0) {
                container.removeConnection(socketAddress);
                socketChannel = (SocketChannel) connection.getChannel();
                selectionKey = socketChannel.keyFor(selector);
                if (selectionKey != null) {
                    selectionKey.attach(null);
                }
                if (!connection.isTerminated()) {
                    connection.close();
                }
            }
        }
    }
