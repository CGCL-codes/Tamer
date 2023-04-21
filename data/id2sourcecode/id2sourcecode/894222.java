    public void refreshChannelHome() {
        rssCb.removeAll();
        String[] names = rssChannelHome.getChannelsNames();
        if (names.length > 0) rssCb.add(rssChannelHome.getChannelsNames());
    }
