    private Vector getChannelVec(Channel p_chan) {
        if (!monitorQueues.containsKey(p_chan.channelName())) monitorQueues.put(p_chan.channelName(), new Vector());
        return ((Vector) monitorQueues.get(p_chan.channelName()));
    }
