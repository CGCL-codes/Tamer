    public void doAction() throws Exception {
        Object thinList = main.find("channels");
        Object thinSelectedChannel = main.getSelectedItem(thinList);
        if (thinSelectedChannel == null) {
            main.status(main.getI18N("i18n.select_channel_unsubscribe"), true);
            return;
        }
        long id = ((Long) main.getProperty(thinSelectedChannel, "id")).longValue();
        ChannelIF channel = DAOChannel.getChannel((long) id);
        boolean confirm = new Confirm(main, main.getI18N("i18n.confirm_02") + "\"" + channel.getTitle() + "\"?", main.getI18N("i18n.unsubscribe_channel")).show();
        if (confirm) {
            DAOChannel.removeChannel(channel);
            main.remove(thinSelectedChannel);
            main.removeAll(main.find("items"));
            new ContentHideAction(main).doAction();
        }
    }
