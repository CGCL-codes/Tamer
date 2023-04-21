    public String getChannelInfo() {
        if (channels == null) {
            int cs = getChannels();
            if (cs == 1) {
                return Config.getResource("songinfo.channel.mono");
            } else if (cs == 2) {
                return Config.getResource("songinfo.channel.stereo");
            } else {
                return Config.getResource("songinfo.unknown.channel");
            }
        }
        return channels;
    }
