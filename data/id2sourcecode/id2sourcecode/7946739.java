    private String getChannels() {
        String channels = "(";
        for (String channel : this.channels) {
            channels += channel + "|";
        }
        return channels.substring(0, channels.length() - 1) + ")";
    }
