    private void onMsgChannelClose(SshMsgChannelClose msg) throws IOException {
        Channel channel = getChannel(msg.getRecipientChannel());
        if (channel == null) {
            throw new IOException("Remote computer tried to close a " + "non existent channel " + String.valueOf(msg.getRecipientChannel()));
        }
        log.info("Remote computer has closed channel " + String.valueOf(channel.getLocalChannelId()) + "[" + channel.getName() + "]");
        if (channel.getState().getValue() != ChannelState.CHANNEL_CLOSED) {
            channel.remoteClose();
        }
    }
