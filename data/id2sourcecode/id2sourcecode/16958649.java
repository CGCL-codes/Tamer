    public void publish(final ChannelEvent event) {
        event.addData("proxy", "true");
        final String channelId = "/" + event.getChannel();
        final Channel channel = getBayeux().getChannel(channelId, true);
        channel.publish(serviceClient, event.getData(), event.getId());
    }
