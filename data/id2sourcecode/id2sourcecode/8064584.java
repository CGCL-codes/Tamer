    private void removeExpiredSubscriptions(Map<String, Subscription> subscriptions) {
        List<Object> channelIds = new ArrayList<Object>(subscriptions.size());
        for (Subscription sub : subscriptions.values()) channelIds.add(GAEGravity.CHANNEL_PREFIX + sub.getChannel().getId());
        Map<Object, Object> channels = gaeCache.getAll(channelIds);
        for (Iterator<Map.Entry<String, Subscription>> ime = subscriptions.entrySet().iterator(); ime.hasNext(); ) {
            Map.Entry<String, Subscription> me = ime.next();
            if (!channels.containsKey(GAEGravity.CHANNEL_PREFIX + me.getValue().getChannel().getId())) ime.remove();
        }
    }
