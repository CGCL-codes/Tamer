    public void doAction() throws Exception {
        Object thinList = main.find("channels");
        Object thinSelectedChannel = main.getSelectedItem(thinList);
        if (thinSelectedChannel == null) {
            main.status(main.getI18N("i18n.error_04"), true);
            return;
        }
        long id = ((Long) main.getProperty(thinSelectedChannel, "id")).longValue();
        ChannelIF channel = DAOChannel.getChannel((long) id);
        boolean confirm = new Confirm(main, main.getI18N("i18n.confirm_01") + "\"" + channel.getTitle() + "\"?", main.getI18N("i18n.clear_history")).show();
        if (confirm) {
            DAOChannel.removeAllItems(channel);
            Object itemsList = main.find("items");
            main.removeAll(itemsList);
            new ContentShowAction(main, channel.getTitle(), channel.getDescription(), channel.getSite()).doAction();
            new ToggleChannelHasUnreadItemsAction(main, thinSelectedChannel, main.find("items")).doAction();
            main.status(null);
        }
    }
