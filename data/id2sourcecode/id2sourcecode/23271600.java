    public void removeChannel(String channel) {
        if (getChannels().contains(channel)) {
            Vector<String> newChannels = new Vector<String>(getChannels());
            newChannels.remove(channel);
            setChannels(newChannels);
        }
    }
