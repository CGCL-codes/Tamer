    public synchronized boolean ensureJoin(String channelName) {
        int id = getChannelId(channelName);
        String chans[] = getChannels(server);
        boolean found = false;
        for (int j = 0; j < chans.length; j++) {
            if (chans[j].equalsIgnoreCase(channelName)) {
                found = true;
                break;
            }
        }
        return found;
    }
