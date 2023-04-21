            public void execute() {
                ChannelDto selected = (ChannelDto) channelTree.getSelectedItem().getUserObject();
                channel.getModelManger().renderModel(getChannelModel(selected));
                channel.setTitle("添加子栏目");
                mainPanel.setWidget(channel);
                contextMenuPanel.hide();
            }
