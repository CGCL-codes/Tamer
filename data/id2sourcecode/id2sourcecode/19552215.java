    void getJoinedChannels(ClientSession session, ChannelRequestC2S request) {
        Iterator<Channel> channels = session.getServer().getChannelManager().getChannels();
        ArrayList<Integer> list = new ArrayList<Integer>();
        while (channels.hasNext()) {
            Channel ch = channels.next();
            if (ch.hasSession(session)) {
                list.add(ch.getID());
            }
        }
        session.send(request, ChannelResponseS2C.create_SUCCEED_GET_JOINED_CHANNELS(list));
    }
