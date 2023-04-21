    public String getChannel() {
        if ((channel != null) && !channel.startsWith("#")) {
            channel = "#" + channel;
        }
        return channel;
    }
