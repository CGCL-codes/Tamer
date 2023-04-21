    private int getDimmerDmxLevel(final long now, final int dimmerIndex) {
        float level = 0;
        Dimmer dimmer = context.getShow().getDimmers().get(dimmerIndex);
        Channel channel = dimmer.getChannel();
        if (channel != null) {
            level = channel.getValue();
        }
        return Dmx.getDmxValue(level);
    }
