    private void onMsgChannelWindowAdjust(SshMsgChannelWindowAdjust msg) throws IOException {
        Channel channel = getChannel(msg.getRecipientChannel());
        if (channel == null) {
            throw new IOException("Remote computer tried to increase " + "window space for non existent channel " + String.valueOf(msg.getRecipientChannel()));
        }
        channel.getRemoteWindow().increaseWindowSpace(msg.getBytesToAdd());
        if (log.isDebugEnabled()) {
            log.debug(String.valueOf(msg.getBytesToAdd()) + " bytes added to remote window");
            log.debug("Remote window space is " + String.valueOf(channel.getRemoteWindow().getWindowSpace()));
        }
    }
