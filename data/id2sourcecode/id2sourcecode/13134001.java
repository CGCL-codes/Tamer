    public final Messenger getChannelMessenger(PeerGroupID redirection, String service, String serviceParam) {
        return new BlockingMessengerChannel(getDestinationAddress(), homeGroupID.equals(redirection) ? null : redirection, service, serviceParam);
    }
