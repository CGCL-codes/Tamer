    public void defaultPatch() {
        Dimmers dimmers = context.getShow().getDimmers();
        Channels channels = context.getShow().getChannels();
        for (int i = 0; i < dimmers.size(); i++) {
            Channel channel = null;
            if (i < channels.size()) {
                channel = channels.get(i);
            }
            dimmers.get(i).setChannel(channel);
        }
    }
