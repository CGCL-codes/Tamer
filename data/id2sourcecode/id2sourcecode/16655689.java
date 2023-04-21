    public void show() throws Exception {
        Object channels = thinlet.find("channels");
        Object selectedChannel = thinlet.getSelectedItem(channels);
        if (selectedChannel == null) {
            ((ThinFeeder) thinlet).status(((ThinFeeder) thinlet).getI18N("i18n.error_15"), true);
            return;
        }
        dialog = thinlet.parse("/net/sourceforge/thinfeeder/widget/channelproperties.xml", this);
        thinlet.add(dialog);
        long id = ((Long) thinlet.getProperty(selectedChannel, "id")).longValue();
        ChannelIF channel = DAOChannel.getChannel(id);
        thinlet.setString(thinlet.find(dialog, "channel_title"), "text", channel.getTitle());
        thinlet.setString(thinlet.find(dialog, "channel_rss"), "text", channel.getLocation() == null ? "" : channel.getLocation().toExternalForm());
    }
