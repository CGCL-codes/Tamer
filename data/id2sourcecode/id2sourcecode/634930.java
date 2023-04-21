    public void disconnected(boolean graceful) {
        setSession(null);
        try {
            ChannelManager channelMgr = AppContext.getChannelManager();
            Channel baseChannel = channelMgr.getChannel(MundojavaServer.BASE_CHANNEL_NAME);
            Event event = new Event(Event.PLAYER_DESTROYED, "id", id);
            baseChannel.send(null, event.toByteBuffer());
        } catch (IOException e) {
            log.log(Level.SEVERE, "Could not sent the PLAYER_DESTROYED event", e);
        }
        String grace = graceful ? "graceful" : "forced";
        log.log(Level.INFO, "User {0} has logged out {1}", new Object[] { id, grace });
    }
