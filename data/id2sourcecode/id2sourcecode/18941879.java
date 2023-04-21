    public void messageIncoming(BusMessage busMessage) {
        String eventState = (String) busMessage.getAttribute(ChannelEvent.event);
        Object event = XMLUtilities.unmarshal(eventState);
        if (event instanceof ChannelEvent) {
            CometService cometService = CometService.getInstance();
            ChannelEvent channelEvent = (ChannelEvent) event;
            cometService.broadcastChannelEvent(channelEvent);
            List<String> updatedChannels = new ArrayList<String>();
            updatedChannels.add(channelEvent.getChannel());
            NetworkEvent networkEvent = new NetworkEvent();
            networkEvent.setUpdatedChannels(updatedChannels);
            List<ChannelBeanMetaData> channelMetaData = (List<ChannelBeanMetaData>) channelEvent.getAttribute(ChannelEvent.metadata);
            networkEvent.setAttribute(ChannelEvent.metadata, channelMetaData);
            cometService.broadcastNetworkEvent(networkEvent);
        }
        busMessage.acknowledge();
    }
