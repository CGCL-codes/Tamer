    private void refreshProperties() {
        channelsLb.removeAll();
        if (rssChannelHome.getChannelsNames().length > 0) channelsLb.add(rssChannelHome.getChannelsNames());
    }
