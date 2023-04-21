    protected void processChatMessage(ChatRequestC2S request, ClientSession session) {
        if (getUserName(session).length() <= 0) {
            session.send(request, ChatResponseS2C.create_UNKNOW());
        }
        try {
            switch(request.Action) {
                case ALL:
                    chatAll(request, session);
                    break;
                case CHANNEL:
                    chatChannel(request, session, getChannelManager().getChannel(request.Channel));
                    break;
                case SINGLE:
                    chatSingle(request, session, containsUser(request.Reciver));
                    break;
            }
        } catch (Exception e) {
            _log.error(e.getMessage(), e);
            session.send(request, ChatResponseS2C.create_UNKNOW());
        }
    }
