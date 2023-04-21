    public String getChannelName(Integer index) {
        String name = "";
        Object obj = channelHash.get(index);
        if (obj != null) {
            ChannelStatus chSt = (ChannelStatus) obj;
            name = chSt.getName();
        }
        return name;
    }
