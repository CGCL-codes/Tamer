    private void onMsgChannelData(SshMsgChannelData msg) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("Received " + String.valueOf(msg.getChannelData().length) + " bytes of data for channel id " + String.valueOf(msg.getRecipientChannel()));
        }
        Channel channel = getChannel(msg.getRecipientChannel());
        channel.processChannelData(msg);
    }
