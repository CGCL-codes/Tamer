    @Override
    public void processUpdate(PlayerList playerList) throws ProtocolException {
        Player player = new Player(getPlayerId(), getNickname());
        Channel channel = playerList.getChannelList().getChannelById(getChannelId());
        if (channel == null) throw new ProtocolException("New Player with unknown channelId " + getChannelId());
        player.getChannelPrivileges().addAll(channelPrivileges);
        player.getServerPrivileges().addAll(serverPrivileges);
        player.getStatus().addAll(status);
        playerList.addPlayer(player);
    }
