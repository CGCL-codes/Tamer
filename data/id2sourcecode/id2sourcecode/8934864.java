    private TideChannel getChannel(String sessionId) {
        TideChannel channel = null;
        Gravity gravity = getGravity();
        if (gravity != null) {
            String channelId = "tide.channel." + sessionId;
            channel = (TideChannel) gravity.getChannel(channelId);
            if (channel == null) {
                channel = new TideChannel(gravity);
                gravity.registerChannel(channel);
            }
        }
        return channel;
    }
