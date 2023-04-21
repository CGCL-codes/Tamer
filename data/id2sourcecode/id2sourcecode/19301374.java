    public static final Channel.ViewResponse getChannelViewResponse(final List<Map.Entry<ContentManager.ChannelSpecification<?>, FutureTask<Channel.ViewResponse>>> channelViewTasks, final ContentManager.ChannelSpecification.Key channelKey) throws WWWeeePortal.Exception, WebApplicationException {
        if (channelKey == null) return null;
        for (Map.Entry<ContentManager.ChannelSpecification<?>, FutureTask<Channel.ViewResponse>> channelViewTask : channelViewTasks) {
            if (channelKey.equals(channelViewTask.getKey().getKey())) {
                return getChannelViewResponse(channelViewTask.getValue());
            }
        }
        return null;
    }
