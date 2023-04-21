    public int getChannelIndex(String chName) {
        Integer chInt = (Integer) channelHash.get(chName);
        if (chInt == null) {
            throw new IllegalArgumentException("Channel by name \"" + chName + "\" does not exist in Client\"" + cliName + "\".");
        }
        return chInt.intValue();
    }
