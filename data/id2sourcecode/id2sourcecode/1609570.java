    public void disconnected(boolean graceful) {
        Channel channel = AppContext.getChannelManager().getChannel("map_" + playerRef.get().getMapId());
        channel.send(null, encodeString("m/loggout/" + playerRef.get().getLoginId() + "/"));
        setSession(null);
        logger.log(Level.INFO, "Disconnected: {0}", this);
        getRoom().removePlayer(this);
        setRoom(null);
        playerRef = null;
    }
