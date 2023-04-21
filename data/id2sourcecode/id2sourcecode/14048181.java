    protected Integer[] getChannelIdsByPaths(String[] paths, Integer siteId) {
        Set<Integer> set = new HashSet<Integer>();
        Channel channel;
        for (String path : paths) {
            channel = channelMng.findByPathForTag(path, siteId);
            if (channel != null) {
                set.add(channel.getId());
            }
        }
        if (set.size() > 0) {
            return set.toArray(new Integer[set.size()]);
        } else {
            return null;
        }
    }
