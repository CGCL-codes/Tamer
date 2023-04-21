    private Channel getActiveChannel(HttpServletRequest request) {
        Channel channel = (Channel) request.getSession().getAttribute(ChannelWebController.ACTIVE_CHANNEL);
        if (channel != null) {
            channel = channelController.getChannel(channel.getId());
        }
        return channel;
    }
