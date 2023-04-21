    public MapleGuildCharacter(MapleCharacter c) {
        name = c.getName();
        level = c.getLevel();
        id = c.getId();
        channel = c.getClient().getChannel();
        jobid = c.getJob().getId();
        guildrank = c.getGuildRank();
        guildid = c.getGuildId();
        online = true;
    }
