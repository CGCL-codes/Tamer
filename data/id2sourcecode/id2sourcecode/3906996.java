    public Channel send(ClientSession sender, ByteBuffer message) {
        getChannel().send(sender, message);
        return this;
    }
