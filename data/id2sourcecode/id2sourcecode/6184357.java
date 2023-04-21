    public String[] getChannels() {
        return Utils.hasNoValue(channels) ? null : Utils.splitString(channels, ",");
    }
