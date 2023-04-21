    private void onMsgChannelExtendedData(SshMsgChannelExtendedData msg) throws IOException {
        Channel channel = getChannel(msg.getRecipientChannel());
        if (channel == null) {
            throw new IOException("Remote computer sent data for non existent channel");
        }
        channel.getLocalWindow().consumeWindowSpace(msg.getChannelData().length);
        channel.processChannelData(msg);
    }
