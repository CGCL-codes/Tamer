    public MapleMessengerCharacter(MapleCharacter maplechar) {
        this.name = maplechar.getName();
        this.channel = maplechar.getClient().getChannel();
        this.id = maplechar.getId();
        this.online = true;
        this.position = 0;
    }
