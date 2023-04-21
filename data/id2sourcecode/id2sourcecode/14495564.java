    String buildTimeSeriesKey(HomenetHardware hardware, Integer channel) {
        return hardware.getChannelDescription(channel) + " [CH-" + channel + "]";
    }
