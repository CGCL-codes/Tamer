    @Override
    public void initWidgets(final DockPanel dockPanel) {
        GWT.runAsync(new CMSRunAsyncCallBack() {

            public void onSuccess() {
                dockPanel.setWidth("100%");
                channel = new ChannelWidget(modelFinishCallback);
                sequenceTree = new SequenceTreeWidget(modelFinishCallback);
                channel.setWidth("100%");
                mainPanel = new SimplePanel();
                dockPanel.add(getChannelTree(), DockPanel.NORTH);
                dockPanel.add(mainPanel, DockPanel.CENTER);
                ChannelManagePage.this.client.success(null);
            }
        });
    }
