    public ServerRoom(long id, int maxPlayers, Player player) {
        this.id = id;
        this.maxPlayers = maxPlayers;
        this.started = false;
        this.players = new ArrayList<ManagedReference<Player>>();
        this.started = false;
        this.gameOver = false;
        availableColors = new ArrayList<Integer>(COLORS);
        Collections.shuffle(availableColors);
        Channel roomChannel = AppContext.getChannelManager().createChannel("room" + id, null, Delivery.RELIABLE);
        roomChannelRef = AppContext.getDataManager().createReference(roomChannel);
        addPlayer(player);
    }
