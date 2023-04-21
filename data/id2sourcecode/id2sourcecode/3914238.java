    public void init() {
        this.removeAll();
        ChannelsetMenuItem nullItem = new ChannelsetMenuItem(VerticalViewer.getInstance().getLocalizedMessage("channelmenu.all"), null);
        this.add(nullItem);
        nullItem.addActionListener(this);
        List channelsetList = Application.getInstance().getChannelsSetsList();
        for (int i = 0; i < channelsetList.size(); i++) {
            TVChannelsSet set = (TVChannelsSet) channelsetList.get(i);
            ChannelsetMenuItem item = new ChannelsetMenuItem(set.getName(), set);
            this.add(item);
            item.addActionListener(this);
        }
        this.addSeparator();
        this.channelMap = new HashMap();
        for (Iterator it = Application.getInstance().getDataStorage().getInfo().channelsList.getChannels().iterator(); it.hasNext(); ) {
            TVChannelsSet.Channel listCh = (TVChannelsSet.Channel) it.next();
            JCheckBoxMenuItem item = new ChannelCheckBoxMenuItem(listCh.getDisplayName(), listCh.getChannelID());
            item.addActionListener(this);
            this.add(item);
            this.channelMap.put(listCh.getChannelID(), item);
        }
    }
