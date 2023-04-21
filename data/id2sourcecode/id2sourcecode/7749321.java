    private JSONArray toJSON(Channels channels) {
        JSONArray jchannels = new JSONArray();
        List<Channel> channelList = channels.getChannels();
        for (Channel channel : channelList) {
            JSONObject jchannel = toJSON(channel);
            jchannels.add(jchannel);
        }
        return jchannels;
    }
