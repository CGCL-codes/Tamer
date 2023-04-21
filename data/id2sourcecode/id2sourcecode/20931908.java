    protected void sendChannelOpenConfirmation(Channel channel) throws IOException {
        SshMsgChannelOpenConfirmation msg = new SshMsgChannelOpenConfirmation(channel.getRemoteChannelId(), channel.getLocalChannelId(), channel.getLocalWindow().getWindowSpace(), channel.getLocalPacketSize(), channel.getChannelConfirmationData());
        transport.sendMessage(msg, this);
    }
