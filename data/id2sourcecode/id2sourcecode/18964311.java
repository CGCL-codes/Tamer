    public Set<Integer> getChannelIds(Integer siteId) {
        Set<Channel> channels = getChannels();
        Set<Integer> ids = new HashSet<Integer>();
        for (Channel c : channels) {
            if (c.getSite().getId().equals(siteId)) {
                ids.add(c.getId());
            }
        }
        return ids;
    }
