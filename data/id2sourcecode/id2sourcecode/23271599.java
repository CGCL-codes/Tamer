    public void addChannel(String channel) {
        Vector<String> newChannels = new Vector<String>(getChannels());
        newChannels.add(channel);
        setChannels(newChannels);
    }
