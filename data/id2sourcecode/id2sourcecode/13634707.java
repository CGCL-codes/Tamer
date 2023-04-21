    public String[] getChannels(IRCServer server) {
        Channel[] inlist = getCoreChannelArrayOnServer(server);
        String[] outlist = new String[inlist.length];
        for (int i = 0; i < inlist.length; i++) {
            outlist[i] = new Character(inlist[i].getScope()).toString() + inlist[i].getName();
        }
        return outlist;
    }
