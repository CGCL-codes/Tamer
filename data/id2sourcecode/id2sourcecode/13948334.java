    public static void doLeaveChannel(SGSServer server, ClientSession session, LeaveChannel lc) {
        try {
            String you = accFromSesName(session.getName());
            SGSClientData l = server.getClientData(you);
            String channelName = l.get(SGSClientData.CURRENT_CHANNEL_NAME, String.class);
            if (!Str.nostr(channelName)) {
                ChannelManager man = AppContext.getChannelManager();
                Channel chnl = man.getChannel(channelName);
                chnl.leave(session);
                onLeaveChannelNotify(server, l.get(SGSClientData.NSERVER_ID, Integer.class), l.get(SGSClientData.NSERVER_CHANNEL_ID, Integer.class), l.get(SGSClientData.CLIENT_SESSION, ClientSession.class), chnl);
                l.set(SGSClientData.CURRENT_CHANNEL_NAME, null);
                l.set(SGSClientData.NSERVER_CHANNEL_ID, Integer.valueOf(Integer.MIN_VALUE));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
