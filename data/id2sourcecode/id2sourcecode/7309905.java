    private DatagramChannel getChannel() throws ClosedChannelException {
        final DatagramChannel channel = this.channel;
        if (channel == null) throw new ClosedChannelException();
        return channel;
    }
