    @Override
    protected void ddOutPageModel(final Object model) {
        ServiceFactory.invoke(ChannelManager.class.getName(), "getChannelTree", new Object[] { null }, new FishAsyncCallback() {

            public void onSuccess(Object o) {
                channelTree.getTree().getModelManger().renderModel(o);
                functionTree.getTree().getModelManger().renderModel(DashMenuBuilder.sequenceMenus(DashMenuBuilder.mergeMenus(MenusFactory.getInstance().getMenu())));
                renderForm(model);
                channelTree.getTree().expandAll(true);
                functionTree.getTree().expandAll(true);
            }
        });
    }
