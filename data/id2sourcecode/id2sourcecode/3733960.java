    public synchronized DirectConnection getConnection(SocketChannel channel) {
        for (int index = 0; index < directConnections.size(); index++) {
            DirectConnection connection = (DirectConnection) directConnections.get(index);
            if (connection.getChannel().equals(channel)) return connection;
        }
        for (int index = 0; index < createdConnections.size(); index++) {
            DirectConnection connection = (DirectConnection) createdConnections.get(index);
            if (connection.getChannel().equals(channel)) return connection;
        }
        return null;
    }
