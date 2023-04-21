    public HomeFrame(GlobalController theModel) {
        manager = theModel;
        channelStampView = new ChannelStampView(manager);
        manager.setChannelStampView((ChannelStampView) channelStampView);
        itemDetailView = new ItemDetailView();
        itemDetailView.setUserObject(theModel);
        theModel.setItemDetailView(itemDetailView);
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        manager.setChannelTree(channelTree);
        channelItemList.setModel(manager.getChannelItemListModel());
        log = Logger.getLogger(this.getClass().getName());
    }
