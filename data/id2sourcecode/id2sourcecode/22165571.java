    public boolean addChannel(String channelName) {
        Channel channel = rbnbController.getChannel(channelName);
        if (channel == null) {
            return false;
        }
        if (channels.contains(channelName)) {
            return false;
        }
        channels.add(channelName);
        String lowerThreshold = channel.getMetadata("lowerbound");
        String upperThreshold = channel.getMetadata("upperbound");
        if (lowerThreshold != null && !lowerThresholds.containsKey(channelName)) {
            try {
                Double.parseDouble(lowerThreshold);
                lowerThresholds.put(channelName, lowerThreshold);
            } catch (java.lang.NumberFormatException nfe) {
                log.warn("Non-numeric lower threshold in metadata: \"" + lowerThreshold + "\"");
            }
        }
        if (upperThreshold != null && !upperThresholds.containsKey(channelName)) {
            try {
                Double.parseDouble(upperThreshold);
                upperThresholds.put(channelName, upperThreshold);
            } catch (java.lang.NumberFormatException nfe) {
                log.warn("Non-numeric upper threshold in metadata: \"" + upperThreshold + "\"");
            }
        }
        log.debug("&&& LOWER: " + lowerThreshold + " UPPER: " + upperThreshold);
        updateTitle();
        channelAdded(channelName);
        rbnbController.subscribe(channelName, this);
        return true;
    }
