    public Set<Channel> getChannels(Integer siteId) {
        Set<Channel> set = getChannels();
        Set<Channel> results = new HashSet<Channel>();
        for (Channel c : set) {
            if (c.getSite().getId().equals(siteId)) {
                results.add(c);
            }
        }
        return results;
    }
