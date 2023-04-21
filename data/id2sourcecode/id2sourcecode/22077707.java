    public Messenger getChannelMessenger(PeerGroupID redirection, String service, String serviceParam) {
        return new ThreadedMessengerChannel(getDestinationAddress(), homeGroupID.equals(redirection) ? null : redirection, service, serviceParam, channelQueueSize, (stateMachine.getState() & (RESOLVED & USABLE)) != 0);
    }
