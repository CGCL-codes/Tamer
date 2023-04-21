    public String[] getChannelsNames() {
        String names[] = (String[]) channelProperties().getKeys().toObjectArray();
        if (names == null) return new String[0];
        return names;
    }
