    private void copyDimmers() {
        Dimmers oldDimmers = oldShow.getDimmers();
        Dimmers newDimmers = newShow.getDimmers();
        int count = Math.min(oldDimmers.size(), newDimmers.size());
        for (int i = 0; i < count; i++) {
            Dimmer oldDimmer = oldDimmers.get(i);
            Dimmer newDimmer = newDimmers.get(i);
            newDimmer.setName(oldDimmer.getName());
            int channelId = oldDimmer.getChannelId();
            if (channelId >= 0) {
                if (channelId < newShow.getNumberOfChannels()) {
                    Channel channel = newShow.getChannels().get(channelId);
                    newDimmer.setChannel(channel);
                }
            }
        }
    }
