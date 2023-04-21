    public void loadValues() {
        Vector faveChannels = new Vector();
        for (int i = 0; i < _options.getFavoriteChannels().getChannelCount(); i++) {
            faveChannels.addElement(_options.getFavoriteChannels().getChannel(i));
        }
        _channelListModel = new _ChannelListModel(faveChannels);
        _channelList.setModel(_channelListModel);
    }
