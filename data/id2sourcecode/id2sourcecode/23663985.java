    public MaplePartyCharacter(MapleCharacter maplechar) {
        this.name = maplechar.getName();
        this.level = maplechar.getLevel();
        this.channel = maplechar.getClient().getChannel();
        this.id = maplechar.getId();
        this.jobid = maplechar.getJob().getId();
        this.mapid = maplechar.getMapId();
        this.online = true;
        this.gender = maplechar.getGender();
        this.married = maplechar.isMarried();
        if (maplechar.getDoors().size() > 0) {
            this.doorTown = maplechar.getDoors().get(0).getTown().getId();
            this.doorTarget = maplechar.getDoors().get(0).getTarget().getId();
            this.doorPosition = maplechar.getDoors().get(0).getTargetPosition();
        }
    }
