    @Override
    public SocketChannel getChannel() {
        final TextNetConnection conn = getTextNetConnection();
        if (conn != null) return conn.getChannel();
        return null;
    }
