    public Widget getChannelTree() {
        channelTree = new ViewTree(provider);
        addEvent();
        ContentPanel wrapper = new ContentPanel("栏目树", true);
        wrapper.setSize("100%", "200px");
        ScrollPanel sp = new ScrollPanel(channelTree);
        sp.setHeight("200px");
        wrapper.addContentWidget(sp);
        return wrapper;
    }
