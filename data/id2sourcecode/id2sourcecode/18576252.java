    private ArrayList getReducedList() {
        if (channels == null) {
            return null;
        }
        ArrayList retVal = new ArrayList();
        retVal.add(Channel.MASTER);
        for (int i = 0; i < channels.size(); i++) {
            Channel c = channels.getChannel(i);
            if (c == this.channel) {
                continue;
            }
            if (isPossibleOut(c, channel.getName())) {
                retVal.add(c.getName());
            }
        }
        return retVal;
    }
