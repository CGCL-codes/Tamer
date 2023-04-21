    protected void doRead(SelectionKey key) throws ConnectionManagerException, InterruptedException {
        SocketConnection connection = (SocketConnection) key.attachment();
        SocketChannel socketChannel = (SocketChannel) connection.getChannel();
        try {
            if (connection != null) {
                if (connection.isTerminated()) {
                    container.sendEvent(new ConnectionEvent(EventCatagory.STATUS, SystemEventType.CLOSE, "closed", container.getName(), connection.getParent(), connection.getName()));
                    container.removeConnection(socketChannel.socket().getRemoteSocketAddress());
                    connection.close();
                    connection = null;
                    key.attach(null);
                } else {
                    connection.readBuffer();
                }
            } else {
                container.sendEvent(new ConnectionManagerEvent(EventCatagory.WARN, SystemEventType.GENERAL, "doRead()- connection is null.", container.getName()));
            }
        } catch (IOException ex) {
            throw new ConnectionManagerException(ex);
        }
    }
