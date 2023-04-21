    public synchronized void callReceiveNetworkActionProcessor(Action action, final Channel channel) {
        reveiveNetworkActionProcessor.perform(action, new ClientTransceiver(channel.getChannelNumber(), channel));
    }
