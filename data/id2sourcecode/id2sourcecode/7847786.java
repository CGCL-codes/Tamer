    public synchronized StatusEvent getChannel(String channel) throws Exception {
        for (int i = 0; i < channels.size(); i++) {
            if (Compare.equal(channel, channels.get(i).getChannel())) {
                return channels.get(i);
            }
        }
        return null;
    }
