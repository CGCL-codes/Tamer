    public void playerToChannel(ServerUser player, Integer channelId) {
        if (channels.containsKey(channelId)) {
            Channel target = channels.get(channelId);
            Channel from = player.getChannel();
            if (target.isOpen()) {
                if (from != null) {
                    from.playerLeft(player, target.getName());
                }
                target.playerJoined(player);
                if (target == lobby) {
                    player.sendMessage(new MessageGameList(createGameInfoList()));
                }
            }
        } else if (channelId.intValue() == -1) {
            Channel from = player.getChannel();
            if (from != null) {
                player.setChannel(null);
                from.playerLeft(player, "nowhere (Single Player)");
                System.out.println("Server::playerToChannel: " + player.getUserInfo().getName() + "-> null");
            }
        }
    }
