    public Channel openChannel(String type) throws JSchException {
        if (!isConnected) {
            throw new JSchException("session is down");
        }
        try {
            Channel channel = Channel.getChannel(type);
            addChannel(channel);
            channel.init();
            return channel;
        } catch (Exception e) {
        }
        return null;
    }
