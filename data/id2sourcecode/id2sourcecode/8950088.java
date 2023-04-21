    @Override
    public void execute(MapleClient c, MessageCallback mc, String[] splittedLine) throws Exception, IllegalCommandSyntaxException {
        MapleCharacter other = c.getChannelServer().getPlayerStorage().getCharacterByName(splittedLine[1]);
        mc.dropMessage(MapleClient.getLogMessage(other, "") + " at X/Y: " + other.getPosition().x + "/" + other.getPosition().y + " MapId: " + other.getMapId() + " MapName: " + other.getMap().getMapName() + " HP: " + other.getHp() + "/" + other.getCurrentMaxHp() + " MP: " + other.getMp() + "/" + other.getCurrentMaxMp() + " EXP: " + other.getExp() + " Job: " + other.getJob().name() + " GuildId: " + other.getGuildId() + " EventStatus: " + (other.getEventInstance() != null) + " PartyStatus: " + (other.getParty() != null) + " TradeStatus: " + (other.getTrade() != null) + " IP: " + other.getClient().getSession().getRemoteAddress().toString().substring(1, (':')) + " Macs: " + other.getClient().getMacs());
    }
