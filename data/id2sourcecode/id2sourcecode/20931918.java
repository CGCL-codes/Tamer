    private void onMsgChannelRequest(SshMsgChannelRequest msg) throws IOException {
        Channel channel = getChannel(msg.getRecipientChannel());
        if (channel == null) {
            log.warn("Remote computer tried to make a request for " + "a non existence channel!");
        }
        channel.onChannelRequest(msg.getRequestType(), msg.getWantReply(), msg.getChannelData());
    }
