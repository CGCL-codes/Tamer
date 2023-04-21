            public void execute() {
                ChannelDto selected = (ChannelDto) channelTree.getSelectedItem().getUserObject();
                channel.getModelManger().renderModel(getChannelModel(selected.getParentExt()));
                channel.setTitle("添加栏目");
                mainPanel.setWidget(channel);
                contextMenuPanel.hide();
                contextMenuPanel.hide();
            }
