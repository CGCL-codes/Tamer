    public void doAction() throws Exception {
        Object channels = main.find("channels");
        main.removeAll(channels);
        List c = DAOChannel.getChannelsOrderByTitle();
        Iterator i = c.iterator();
        while (i.hasNext()) {
            ChannelIF channel = (ChannelIF) i.next();
            new PopulateChannelAction(main, channel).doAction();
        }
    }
