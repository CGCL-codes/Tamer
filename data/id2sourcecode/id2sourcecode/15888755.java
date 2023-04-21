    private void publish(IContribution contribution, String channelName) {
        Channel channel = CometClientServlet.getBayeux().getChannel(channelName, false);
        if (channel != null) {
            channel.publish(CometClientServlet.getClient(), JSONConverter.convertToJSON(contribution), channelName);
        }
    }
