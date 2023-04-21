        public void itemAdded(ItemIF newItem) {
            DefaultTreeModel treeModel = channelTree.getTreeModel();
            ChannelTreeNode node = channelTree.getChannelTreeNode(newItem.getChannel());
            node.update();
            treeModel.nodeChanged(node);
            if (itemTable.getItemModel().getChannel().equals(newItem.getChannel())) {
                itemTable.getItemModel().addItem(newItem);
            }
        }
