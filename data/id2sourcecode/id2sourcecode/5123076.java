    @Override
    public void processUpdate(PlayerList playerList) throws ProtocolException {
        Player p = playerList.get(getPlayerId());
        if (p == null) {
            throw new ProtocolException("Recieved PlayerChannelChange for unknown player with id " + playerId);
        }
        ChannelList cl = playerList.getChannelList();
        Channel channelFrom = cl.getChannelById(getFromChannelId());
        Channel channelTo = cl.getChannelById(getToChannelId());
        if (channelFrom == null || channelTo == null) {
            throw new ProtocolException("Recieved PlayerChannelChange for at least one unknown Channel with ids [from:" + getFromChannelId() + " , to:" + getToChannelId() + "]");
        }
        if (p.getCurrentChannel() != channelFrom) {
            throw new ProtocolException("Recieved PlayerChannelChange for player " + getPlayerId() + " with wrong data for current channel [known:" + p.getCurrentChannel().getId() + " , recieved:" + getFromChannelId() + "]");
        }
        p.setCurrentChannel(channelTo);
    }
