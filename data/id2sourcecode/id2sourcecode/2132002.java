    public MapleCharacter getCharByName(String namee) {
        try {
            return getClient().getChannelServer().getPlayerStorage().getCharacterByName(namee);
        } catch (Exception e) {
            return null;
        }
    }
