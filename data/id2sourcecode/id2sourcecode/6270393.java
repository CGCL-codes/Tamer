    private void copyChannels() {
        Channels oldChannels = oldShow.getChannels();
        Channels newChannels = newShow.getChannels();
        int count = Math.min(oldChannels.size(), newChannels.size());
        for (int i = 0; i < count; i++) {
            Channel oldChannel = oldChannels.get(i);
            Channel newChannel = newChannels.get(i);
            newChannel.setName(oldChannel.getName());
        }
    }
