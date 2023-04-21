    protected void processChannelMessage(ChannelRequestC2S request, ClientSession session) {
        if (getUserName(session).length() <= 0) {
            session.send(request, ChatResponseS2C.create_UNKNOW());
        }
        try {
            switch(request.Action) {
                case GET_JOINED_CHANNELS:
                    getJoinedChannels(session, request);
                    break;
                case GET_CHANNEL_MEMBERS:
                    getChannelMembers(getChannelManager().getChannel(request.ChannelName), session, request);
                    break;
                case CREATE_CHANNEL:
                    createChannel(session, request);
                    break;
                case JOIN_CHANNEL:
                    joinChannel(getChannelManager().getChannel(request.ChannelName), session, request);
                    break;
                case LEAVE_CHANNEL:
                    leaveChannel(getChannelManager().getChannel(request.ChannelName), session, request);
                    break;
            }
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
            session.send(request, ChatResponseS2C.create_UNKNOW());
        }
    }
