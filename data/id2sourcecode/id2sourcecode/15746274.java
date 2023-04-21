    protected void channelAdded(String channelName) {
        String labelText = channelName;
        Channel channel = rbnbController.getChannel(channelName);
        String unit = null;
        if (channel != null) {
            unit = channel.getMetadata("units");
            if (unit != null) {
                labelText += "(" + unit + ")";
            }
        }
        String lowerThresholdString = (String) (lowerThresholds.get(channelName));
        String upperThresholdString = (String) (upperThresholds.get(channelName));
        double lowerThreshold = Double.NEGATIVE_INFINITY;
        double upperThreshold = Double.POSITIVE_INFINITY;
        if (lowerThresholdString != null) {
            try {
                lowerThreshold = Double.parseDouble(lowerThresholdString);
            } catch (java.lang.NumberFormatException nfe) {
                log.warn("Non-numeric lower threshold in metadata: " + lowerThresholdString);
            }
        }
        if (upperThresholdString != null) {
            try {
                upperThreshold = Double.parseDouble(upperThresholdString);
            } catch (java.lang.NumberFormatException nfe) {
                log.warn("Non-numeric upper threshold in metadata: " + upperThresholdString);
            }
        }
        int tableNumber = channelTableMap.get(channelName);
        tableModels.get(tableNumber).addRow(channelName, unit, lowerThreshold, upperThreshold);
        properties.setProperty("channelTable_" + channelName, Integer.toString(tableNumber));
    }
