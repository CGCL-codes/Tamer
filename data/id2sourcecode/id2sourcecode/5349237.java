    Channel getChannel(final String signalName) throws gov.aps.jca.CAException {
        Channel channel;
        synchronized (_channelMap) {
            channel = _channelMap.get(signalName);
            if (channel == null) {
                channel = _jcaSystem.getJcaContext().createChannel(signalName);
                _channelMap.put(signalName, channel);
            }
        }
        return channel;
    }
