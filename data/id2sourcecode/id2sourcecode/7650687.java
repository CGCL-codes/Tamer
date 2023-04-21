    public void doAction() throws Exception {
        if (equalChannels(thinSelectedChannel, main.getSelectedItem(main.find("channels")))) main.removeAll(thinItems);
        long id = ((Long) main.getProperty(thinSelectedChannel, "id")).longValue();
        ChannelIF channel = DAOChannel.getChannel((long) id);
        new ShowChannelPropertiesAction(main, channel).doAction();
        new ShowChannelItemsAction(main, thinSelectedChannel, channel, thinItems).doAction();
        new ToggleChannelHasUnreadItemsAction(main, channel, thinSelectedChannel).doAction();
    }
