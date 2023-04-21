    protected GMChannelRoute getChannelRoute(int channelId) {
        GMChannelRoute gmChannelRoute = this.channelRouter.getRoute(channelId);
        if (gmChannelRoute == null) {
            gmChannelRoute = new GMChannelRoute(GMChannelRoute.NULL_VALUE);
            gmChannelRoute.setChannel1(15);
            gmChannelRoute.setChannel2(15);
        }
        return gmChannelRoute;
    }
