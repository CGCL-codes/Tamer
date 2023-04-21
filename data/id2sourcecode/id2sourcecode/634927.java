    public static ServerPlayer loggedIn(ClientSession session) {
        String playerId = session.getName();
        DataManager dataMgr = AppContext.getDataManager();
        ServerPlayer player;
        try {
            player = (ServerPlayer) dataMgr.getBinding(playerId);
        } catch (NameNotBoundException e) {
            float red = RandomUtil.randColorVal();
            float green = RandomUtil.randColorVal();
            float blue = RandomUtil.randColorVal();
            player = new ServerPlayer(playerId, 0.0f, 0.0f, 0.0f, 0.0f, red, green, blue);
            dataMgr.setBinding(playerId, player);
            try {
                PlayersList playerList = (PlayersList) dataMgr.getBinding(MundojavaServer.PLAYERS_LIST_NAME);
                playerList.add(player);
            } catch (NameNotBoundException e1) {
                log.log(Level.SEVERE, "The players list does not exist.", e1);
            } catch (IOException e1) {
                log.log(Level.SEVERE, "Error unpacking the message", e1);
            }
        }
        player.setSession(session);
        ChannelManager channelMgr = AppContext.getChannelManager();
        Channel baseChannel = channelMgr.getChannel(MundojavaServer.BASE_CHANNEL_NAME);
        baseChannel.join(session);
        try {
            Event event = new Event(Event.PLAYER_CREATED, "id", player.getId(), "x", player.getX(), "y", player.getY(), "z", player.getZ(), "orientation", player.getOrientation(), "red", player.getRed(), "green", player.getGreen(), "blue", player.getBlue());
            baseChannel.send(null, event.toByteBuffer());
        } catch (IOException e) {
            log.log(Level.SEVERE, "Could not sent the PLAYER_CREATED event", e);
        }
        return player;
    }
