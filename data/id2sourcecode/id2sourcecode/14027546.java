    public void normalize() {
        Set ids = new TreeSet();
        Iterator it = allChannels.getChannels().iterator();
        while (it.hasNext()) {
            TVChannelsSet.Channel ch = (TVChannelsSet.Channel) it.next();
            ids.add(ch.getChannelID());
        }
        it = selectedChannelIDs.iterator();
        while (it.hasNext()) {
            if (!ids.contains(it.next())) {
                it.remove();
            }
        }
    }
