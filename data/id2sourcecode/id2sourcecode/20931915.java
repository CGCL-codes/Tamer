    private void onMsgChannelEOF(SshMsgChannelEOF msg) throws IOException {
        Channel channel = getChannel(msg.getRecipientChannel());
        try {
            log.info("Remote computer has set channel " + String.valueOf(msg.getRecipientChannel()) + " to EOF [" + channel.getName() + "]");
            channel.setRemoteEOF();
        } catch (IOException ioe) {
            log.info("Failed to close the ChannelInputStream after EOF event");
        }
    }
