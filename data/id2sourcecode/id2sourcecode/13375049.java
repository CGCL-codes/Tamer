    protected void rebuildChannelStripMap() {
        HashMap<String, HashSet<Strip>> newMap = new HashMap<String, HashSet<Strip>>();
        Iterator<Strip> stripIt = stripList.iterator();
        while (stripIt.hasNext()) {
            final Strip strip = stripIt.next();
            Iterator<StripChannel> channelIt = strip.getChannelList().iterator();
            while (channelIt.hasNext()) {
                final StripChannel channel = channelIt.next();
                final String channelId = channel.getChannelID();
                HashSet<Strip> newStripSet = newMap.get(channelId);
                if (newStripSet == null) {
                    newStripSet = new HashSet<Strip>();
                    newMap.put(channelId, newStripSet);
                }
                newStripSet.add(strip);
            }
        }
        channelToStripMap.clear();
        channelToStripMap.putAll(newMap);
    }
