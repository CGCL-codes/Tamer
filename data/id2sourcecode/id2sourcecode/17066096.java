    @Override
    protected void add(Date periodStart, ChannelStats stat) {
        Map<String, ChannelStats> map = get(periodStart);
        if (map == null) {
            map = new HashMap<String, ChannelStats>();
            put(periodStart, map);
        }
        ChannelStats existing = map.get(stat.getChannelId());
        if (existing == null) {
            map.put(stat.getChannelId(), stat);
        } else {
            existing.add(stat);
        }
    }
