    public void updateChannels(List<Channel> lChannel) {
        htChannel = new Hashtable<Integer, Channel>();
        for (Channel c : lChannel) {
            htChannel.put(c.getChannelNumber(), c);
        }
        logger.debug("Update Channel List. Elements: " + htChannel.size());
    }
