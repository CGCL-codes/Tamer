    private ChannelsRunnable getAvailableRunnable() {
        Iterator<Runnable> iterator = queue.iterator();
        while (iterator.hasNext()) {
            ChannelsRunnable runnable = (ChannelsRunnable) iterator.next();
            RegisterableChannel channel = runnable.getChannel();
            if (!runningChannels.contains(channel)) {
                runningChannels.add(channel);
                return new QueueRunnable(runningChannels, runnable);
            }
        }
        return null;
    }
