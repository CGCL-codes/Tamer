    public void addMarkers(MapWidget map) {
        List<ChannelOutput> channelOutputs = model.getOutputs().getChannelOutputs();
        for (ChannelOutput channelOutput : channelOutputs) {
            double distance = 0.0;
            try {
                distance = Double.parseDouble(channelOutput.distance);
            } catch (NumberFormatException nfe) {
                if ("length".equals(channelOutput.distance)) {
                    Channel channel = model.getChannels().getChannel(channelOutput.channelId);
                    if (channel == null) {
                        continue;
                    }
                    distance = channel.getLength();
                }
            }
            LatLng position = calculateMarkerLocation(channelOutput.channelId, distance);
            if (position == null) {
                continue;
            }
            addOutputMarkerToMap(map, position, channelOutput);
        }
    }
