    public void ok() throws Exception {
        Object channels = thinlet.find("channels");
        Object selectedChannel = thinlet.getSelectedItem(channels);
        Object channelTitle = thinlet.find("channel_title");
        String channelTitleStr = thinlet.getString(channelTitle, "text");
        long id = ((Long) thinlet.getProperty(selectedChannel, "id")).longValue();
        ChannelIF channel = DAOChannel.getChannel(id);
        channel.setTitle(channelTitleStr);
        DAOChannel.updateChannel(channel);
        thinlet.setString(selectedChannel, "text", channelTitleStr);
        closeDialog(dialog);
    }
