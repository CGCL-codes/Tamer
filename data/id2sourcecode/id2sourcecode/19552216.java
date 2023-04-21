    void getChannelMembers(Channel dstChannel, ClientSession session, ChannelRequestC2S request) {
        if (dstChannel != null) {
            Iterator<ClientSession> iter = dstChannel.getSessions();
            ArrayList<String> list = new ArrayList<String>();
            while (iter.hasNext()) {
                ClientSession member = iter.next();
                list.add(getUserName(member));
            }
            session.send(request, ChannelResponseS2C.create_SUCCEED_GET_CHANNEL_MEMBERS(list));
        } else {
            session.send(request, ChannelResponseS2C.create_FAILED(request.Action));
        }
    }
