    public String getChannelMask(String str) {
        for (int i = 0; i < channels.length; i++) if (channels[i][1].equalsIgnoreCase(str)) return channels[i][2];
        return "";
    }
