    public Channel getChannel(String handle) {
        Channel channel = channelMap.get(handle);
        if (channel == null) {
            String signal = getSignal(handle);
            if (signal != null) {
                ValueTransform transform = getTransform(handle);
                if (transform != null) {
                    channel = _channelFactory.getChannel(signal, transform);
                } else {
                    channel = _channelFactory.getChannel(signal);
                }
            }
            if (channel != null) {
                channelMap.put(handle, channel);
            }
        }
        return channel;
    }
