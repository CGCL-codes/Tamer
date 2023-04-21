    public void receive(DPWSContextImpl context, InMessage message) {
        if (message.getChannel() == null) message.setChannel(this);
        getEndpoint().onReceive(context, message);
    }
