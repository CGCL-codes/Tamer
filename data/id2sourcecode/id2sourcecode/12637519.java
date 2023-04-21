    public void doAction() {
        main.status(main.getI18N("i18n.refreshing_all_feeds"));
        int channelsUpdated = 0;
        int itemsUpdated = 0;
        int channelsError = 0;
        try {
            List channels = DAOChannel.getChannelsOrderByTitle();
            Iterator i = channels.iterator();
            while (i.hasNext()) {
                if (stopped) {
                    main.status(null);
                    break;
                }
                ChannelIF channel = (ChannelIF) i.next();
                main.status(main.getI18N("i18n.refreshing_feed") + (channel.getLocation() == null ? channel.getTitle() : channel.getLocation().toExternalForm()) + main.getI18N("i18n...."));
                try {
                    itemsUpdated += DAOChannel.updateChannelItems(channel);
                    if (DAOChannel.hasUnreadItems(channel)) {
                        Object channelNode = getChannelNode(channel);
                        if (channelNode != null) new SetBoldFontAction(main, channelNode).doAction();
                    }
                    channelsUpdated++;
                } catch (Throwable t) {
                    t.printStackTrace();
                    main.status(main.getI18N("i18n.error_08") + channel.getLocation().toExternalForm() + "!", true);
                    channelsError++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            main.status(main.getI18N("i18n.error_09"), true);
        }
        main.status(main.getI18N("i18n.feeds_refreshed") + channelsUpdated + " " + main.getI18N("i18n.refresh_1") + itemsUpdated + " " + main.getI18N("i18n.refresh_2") + channelsError + " " + main.getI18N("i18n.refresh_3"));
    }
