    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable> ArrayList<ChannelMessage<T>> readAll(String channelName) {
        final long deadline = System.currentTimeMillis() + 28 * 1000;
        final ArrayList<T> result = new ArrayList<T>();
        final Channel<T> channel = ChannelServiceFactory.getChannelService().getChannel(channelName, true);
        while (System.currentTimeMillis() < deadline && !result.isEmpty()) {
            result.addAll(channel.readAll());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                break;
            }
        }
        return (ArrayList<ChannelMessage<T>>) result;
    }
