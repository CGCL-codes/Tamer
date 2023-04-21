    private Widget getAccessChannel() {
        final AccessChannelTreeWidget t = new AccessChannelTreeWidget();
        ServiceFactory.invoke(ChannelManager.class.getName(), "getChannelTree", new Object[] { null }, new FishAsyncCallback() {

            public void onSuccess(Object o) {
                t.getTree().getModelManger().renderModel(o);
            }
        });
        return t;
    }
