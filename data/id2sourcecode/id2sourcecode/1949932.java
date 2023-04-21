    public void configureRouter(TGSong tgSong) {
        this.router.resetRoutes();
        Iterator tgChannels = tgSong.getChannels();
        while (tgChannels.hasNext()) {
            TGChannel tgChannel = (TGChannel) tgChannels.next();
            GMChannelRoute cmChannelRoute = new GMChannelRoute(tgChannel.getChannelId());
            cmChannelRoute.setChannel1(getIntegerChannelParameter(tgChannel, GMChannelRoute.PARAMETER_GM_CHANNEL_1, GMChannelRoute.NULL_VALUE));
            cmChannelRoute.setChannel2(getIntegerChannelParameter(tgChannel, GMChannelRoute.PARAMETER_GM_CHANNEL_2, GMChannelRoute.NULL_VALUE));
            this.router.configureRoutes(cmChannelRoute, tgChannel.isPercussionChannel());
        }
    }
