    public void update(QueryInfo info) {
        date = new Date();
        ping = (int) info.getPing();
        playerCount = info.getPlayers().size();
        channelCount = info.getChannels().size();
        int activePlayerCount = 0;
        int activeChannelCount = 0;
        for (PlayerInfo player : info.getPlayers()) {
            if (player.isPlaying()) {
                activePlayerCount++;
            }
        }
        for (ChannelInfo channel : info.getChannels()) {
            if (channel.isPlaying()) {
                activeChannelCount++;
            }
        }
        this.activePlayerCount = activePlayerCount;
        this.activeChannelCount = activeChannelCount;
    }
