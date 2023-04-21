    public Collection<Long> getChannelIds() {
        CmsChannel c = getChannel();
        Collection<Long> ids = new ArrayList<Long>();
        if (c != null) {
            while (c != null) {
                ids.add(c.getId());
                c = c.getParent();
            }
        }
        return ids;
    }
