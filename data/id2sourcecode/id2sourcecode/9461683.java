    public void removePlayer(Player player) {
        ManagedReference<Player> playerRef = AppContext.getDataManager().createReference(player);
        if (!started && playerRef.equals(players.get(0)) && players.size() > 1) {
            players.get(1).get().getClientSession().send(Protocol.gameOwner());
        }
        players.remove(playerRef);
        System.out.println("removePlayer " + player.getName() + " from room" + id + ", remaining: " + players.size());
        if (players.size() == 0) {
            AppContext.getTaskManager().scheduleTask(new DropRoomTask(id));
        } else {
            ServerFigure figure = player.getFigure();
            FigureInfo figInfo = null;
            if (figure != null) {
                figInfo = getFigureInfo(figure);
                figure.clear();
            }
            getChannel().send(null, Protocol.roomLeft(player.getPlayerInfo(), figInfo));
            getChannel().leave(player.getClientSession());
            availableColors.add(player.getColor());
        }
    }
