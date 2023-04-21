    protected void doDisconnect(String name) throws ConnectionManagerException {
        SocketAddress socketAddress = null;
        SocketChannel sc = null;
        Initiator initiator = container.getInitiator(name);
        sc = (SocketChannel) initiator.getChannel();
        socketAddress = sc.socket().getLocalSocketAddress();
        SelectionKey key = sc.keyFor(selector);
        if (key != null && key.attachment() instanceof SocketConnection) {
            SocketConnection connection = (SocketConnection) key.attachment();
            try {
                if (connection != null) {
                    container.removeConnection(socketAddress);
                    container.sendEvent(new InitiatorEvent(EventCatagory.STATUS, SystemEventType.DISCONNECT, sc.socket().getLocalSocketAddress().toString(), container.getName(), name));
                    connection.close();
                    connection = null;
                    key.attach(null);
                }
            } catch (IOException ex) {
                throw new ConnectionManagerException(ex);
            }
        } else {
            container.sendEvent(new ConnectionManagerEvent(EventCatagory.ERROR, SystemEventType.DISCONNECT, "Selection Key is not of type Initiator", container.getName()));
        }
    }
