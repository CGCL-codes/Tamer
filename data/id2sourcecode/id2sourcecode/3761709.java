    @SuppressWarnings("unchecked")
    @Override
    public <T extends Serializable> ArrayList<ChannelMessage<T>> readAll(String channelName) {
        final ArrayList<T> result = new ArrayList<T>();
        final Channel<T> channel = ChannelServiceFactory.getChannelService().<T>getChannel(channelName, false);
        long deadline = System.currentTimeMillis() + 27 * 1000;
        while (System.currentTimeMillis() < deadline && result.isEmpty()) {
            result.addAll(channel.readAll());
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                break;
            }
        }
        return (ArrayList<ChannelMessage<T>>) result;
    }
