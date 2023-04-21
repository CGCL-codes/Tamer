    public void channelRemoved(ConferenceCall conferenceCall, Channel channel) {
        if (logger.isDebugEnabled()) logger.debug("channelRemoved " + channel);
        if (conferenceCall.getChannels().size() == 1) {
            unmonitorConference(conferenceCall.getRoomId());
            System.out.println("unmonitorConference " + conferenceCall.getRoomId());
        }
    }
