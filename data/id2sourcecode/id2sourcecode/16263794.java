    private Channel getChannel(RemoteFile file) throws JSchException {
        if (file.getChannel() == null || file.getChannel().isClosed()) {
            Connection connection = file.getConnection();
            Channel channel = getChannel(connection);
            file.setChannel(channel);
        } else if (!file.getChannel().isConnected()) {
            file.getChannel().connect();
        }
        return file.getChannel();
    }
