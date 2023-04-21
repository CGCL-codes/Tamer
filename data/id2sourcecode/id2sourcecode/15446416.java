    public ChannelStatus getChannelStatus(Channel channel, Locale locale) throws ChannelUnknownException {
        final Tracer tracer = baseTracer.entering("getChannelStatus(Channel channel, Locale locale)");
        boolean channelMatch = false;
        Channel savedChannel = null;
        String channelID = "null";
        Exception cause = null;
        ChannelStatus channelStatus = null;
        try {
            channelID = channel.getObjectId();
            LinkedList channels = null;
            channels = channel.getDirection() == Direction.OUTBOUND ? this.outboundChannels : this.inboundChannels;
            synchronized (this) {
                for (int i = 0; i < channels.size(); i++) {
                    savedChannel = (Channel) channels.get(i);
                    if (savedChannel.getObjectId().equalsIgnoreCase(channelID)) {
                        channelMatch = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            tracer.catched(e);
            cause = e;
            tracer.error("Channel not found in lookup. Reason: " + e.getMessage());
        }
        if (!channelMatch) {
            ChannelUnknownException cue = new ChannelUnknownException("Channel with ID " + channelID + " not found.", cause);
            tracer.error("Channel {0} is not found.", channelID);
            tracer.throwing(cue);
            throw cue;
        }
        ChannelStatusFactory channelStatusFactory = ChannelStatusFactory.getInstance();
        if (channelStatusFactory == null) {
            ChannelUnknownException cue = new ChannelUnknownException("Internal error: Unable to get instance of ChannelStatusFactory.", cause);
            tracer.error("Unable to get instance of ChannelStatusFactory.");
            tracer.throwing(cue);
            throw cue;
        }
        try {
            String channelAdapterStatus = channel.getValueAsString("adapterStatus");
            if ((channelAdapterStatus == null) || !(channelAdapterStatus.equalsIgnoreCase("active"))) {
                channelStatus = channelStatusFactory.createChannelStatus(channel, ChannelState.INACTIVE, channelLocalizer.localizeString("CHANNEL_INACTIVE", locale));
            } else {
                channelStatus = channelStatusFactory.createChannelStatus(channel, ChannelState.OK, this.channelLocalizer.localizeString("CHANNEL_OK", locale));
            }
        } catch (Exception e) {
            tracer.catched(e);
            tracer.error("Channel status {0} cannot be determinded. Exception is: {1}", new Object[] { channel.getChannelName(), e.getMessage() });
            channelStatus = channelStatusFactory.createChannelStatus(channel, ChannelState.ERROR, "Channel status cannot be determined due to: " + e.getMessage());
        }
        tracer.leaving();
        return channelStatus;
    }
