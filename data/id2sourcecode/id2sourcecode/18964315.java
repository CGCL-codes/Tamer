    public void addToChannels(Channel channel) {
        if (channel == null) {
            return;
        }
        Set<Channel> set = getChannels();
        if (set == null) {
            set = new HashSet<Channel>();
            setChannels(set);
        }
        set.add(channel);
    }
