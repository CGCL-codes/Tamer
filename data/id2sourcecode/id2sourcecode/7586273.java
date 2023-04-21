    @Override
    public void channelUser(Connection source, BNetUser user) {
        if (!lockdownPossible && source.getProfile().isOneOfMyUsers(user) && ((user.getFlags() & 2) != 0) && (source.getChannel() != null) && source.getChannel().startsWith("Clan ")) {
            lockdownPossible = true;
            source.sendChat("/c pub");
            source.sendChat("/o unigpub");
        }
    }
