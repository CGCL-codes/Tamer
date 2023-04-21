    private void onMsgChannelOpen(SshMsgChannelOpen msg) throws IOException {
        log.info("Request for " + msg.getChannelType() + " channel recieved");
        ChannelFactory cf = (ChannelFactory) allowedChannels.get(msg.getChannelType());
        if (cf == null) {
            sendChannelOpenFailure(msg.getSenderChannelId(), SshMsgChannelOpenFailure.SSH_OPEN_CONNECT_FAILED, "The channel type is not supported", "");
            log.info("Request for channel type " + msg.getChannelType() + " refused");
            return;
        }
        try {
            log.info("Creating channel " + msg.getChannelType());
            Channel channel = cf.createChannel(msg.getChannelType(), msg.getChannelData());
            log.info("Initiating channel");
            Long channelId = getChannelId();
            channel.init(this, channelId.longValue(), msg.getSenderChannelId(), msg.getInitialWindowSize(), msg.getMaximumPacketSize());
            activeChannels.put(channelId, channel);
            log.info("Sending channel open confirmation");
            sendChannelOpenConfirmation(channel);
            channel.open();
        } catch (InvalidChannelException ice) {
            sendChannelOpenFailure(msg.getSenderChannelId(), SshMsgChannelOpenFailure.SSH_OPEN_CONNECT_FAILED, ice.getMessage(), "");
        }
    }
