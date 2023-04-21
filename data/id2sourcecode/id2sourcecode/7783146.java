    public int getChannelPriority() {
        final String channelPrefix = StripChannelGroup.getChannelPrefix(getChannelName());
        if (channelPrefix == null) return 0;
        final String channelSuffix = StripChannelGroup.getChannelSuffix(getChannelName());
        if (channelSuffix == null) return 0;
        if (channelSuffix.contains(TOTALSUFFIX) && getType() != AVGTYPE) return 0;
        if (channelSuffix.contains(MAINSUFFIX)) return 0;
        int suffixDigits = 0;
        try {
            suffixDigits = extractFinalDigits(channelSuffix) + 1;
        } catch (NumberFormatException e) {
            suffixDigits = 5;
        }
        return suffixDigits;
    }
