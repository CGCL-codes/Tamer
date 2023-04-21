    private void updateTimeBounds() {
        List<String> selectedChannels = getSelectedChannels();
        if (selectedChannels.size() == 0) {
            return;
        }
        double minimum = Double.MAX_VALUE;
        double maximum = 0;
        for (String channelName : selectedChannels) {
            Channel channel = RBNBController.getInstance().getChannel(channelName);
            if (channel == null) {
                continue;
            }
            double channelStart = Double.parseDouble(channel.getMetadata("start"));
            double channelDuration = Double.parseDouble(channel.getMetadata("duration"));
            double channelEnd = channelStart + channelDuration;
            if (channelStart < minimum) {
                minimum = channelStart;
            }
            if (channelEnd > maximum) {
                maximum = channelEnd;
            }
        }
        timeSlider.setValues(minimum, maximum);
    }
