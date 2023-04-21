    public TVChannel getRealChannel(Channel chinfo) {
        Iterator it = data.getChannelsIterator();
        while (it.hasNext()) {
            TVChannel ch = (TVChannel) it.next();
            if (ch.getID().equals(chinfo.getChannelID())) {
                return ch;
            }
        }
        return null;
    }
