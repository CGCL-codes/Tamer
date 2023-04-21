    void createChannel(ClientSession session, ChannelRequestC2S request) {
        Channel dstChannel = session.getServer().getChannelManager().getChannel(request.ChannelName);
        if (dstChannel == null) {
            dstChannel = getChannelManager().createChannel(request.ChannelName, new GTChannelListener());
            session.send(request, ChannelResponseS2C.create_SUCCEED_CREATE_CHANNEL(dstChannel.getID()));
        } else {
            session.send(request, ChannelResponseS2C.create_FAILED(request.Action));
        }
    }
