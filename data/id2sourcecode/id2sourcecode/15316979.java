    private void initTreeContextMenu() {
        treeContextMenu = new MyMenuBar(true);
        MyMenuItem edit = new MyMenuItem("编辑", new Command() {

            public void execute() {
                channel.getModelManger().renderModel(channelTree.getSelectedItem().getUserObject());
                channel.setTitle("编辑栏目");
                mainPanel.setWidget(channel);
                contextMenuPanel.hide();
            }
        });
        MyMenuItem delete = new MyMenuItem("删除", new Command() {

            public void execute() {
                Message.confirm("当前栏目的子栏目和内容将都被删除，是否继续？", new IMessageConfirmCall() {

                    public void doExcute(boolean flag) {
                        if (flag) {
                            ServiceFactory.invoke(ChannelManager.class.getName(), "delete", new Object[] { channelTree.getSelectedItem().getUserObject() }, new FishAsyncCallback() {

                                public void onSuccess(Object o) {
                                    channelTree.deleteItem();
                                }
                            });
                        }
                    }
                });
                contextMenuPanel.hide();
            }
        });
        MyMenuItem sequence = new MyMenuItem("同级排序", new Command() {

            public void execute() {
                List sequenceChannels;
                if (channelTree.getSelectedItem().getParentItem() != null) {
                    ChannelDto parentChannel = (ChannelDto) channelTree.getSelectedItem().getParentItem().getUserObject();
                    sequenceChannels = parentChannel.getChildren();
                } else {
                    sequenceChannels = (List) channelTree.getModelManger().getModel();
                }
                sequenceTree.getTree().getModelManger().renderModel(ReflectionHelper.cloneProperty(sequenceChannels));
                mainPanel.setWidget(sequenceTree);
                contextMenuPanel.hide();
            }
        });
        MyMenuItem add = new MyMenuItem("添加同级节点", new Command() {

            public void execute() {
                ChannelDto selected = (ChannelDto) channelTree.getSelectedItem().getUserObject();
                channel.getModelManger().renderModel(getChannelModel(selected.getParentExt()));
                channel.setTitle("添加栏目");
                mainPanel.setWidget(channel);
                contextMenuPanel.hide();
                contextMenuPanel.hide();
            }
        });
        MyMenuItem addChild = new MyMenuItem("添加子节点", new Command() {

            public void execute() {
                ChannelDto selected = (ChannelDto) channelTree.getSelectedItem().getUserObject();
                channel.getModelManger().renderModel(getChannelModel(selected));
                channel.setTitle("添加子栏目");
                mainPanel.setWidget(channel);
                contextMenuPanel.hide();
            }
        });
        treeContextMenu.addItem(add);
        treeContextMenu.addItem(addChild);
        treeContextMenu.addItem(edit);
        treeContextMenu.addItem(sequence);
        treeContextMenu.addItem(delete);
        contextMenuPanel = new PopupPanel(true);
        contextMenuPanel.setWidget(treeContextMenu);
    }
