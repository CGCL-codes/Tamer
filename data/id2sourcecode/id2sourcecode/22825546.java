    private String getChannelFormated() {
        String str = (channel.intValue() < 10) ? "0" : "";
        return str + channel;
    }
