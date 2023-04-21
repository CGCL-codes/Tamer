    private String getChannelDisplay(String channelName) {
        String seriesName = channelName;
        Channel channel = rbnbController.getChannel(channelName);
        if (channel != null) {
            String unit = channel.getMetadata("units");
            if (unit != null) {
                seriesName += " (" + unit + ")";
            }
        }
        return seriesName;
    }
