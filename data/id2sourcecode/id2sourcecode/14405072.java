        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int row = getRowForLocation(x, y);
            TreePath path = getPathForRow(row);
            if (path != null) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                TVChannelsSet.Channel channel = (TVChannelsSet.Channel) node.getUserObject();
                if (channel != null) {
                    final String channelID = channel.getChannelID();
                    if (channels.selectedChannelIDs.contains(channelID)) {
                        channels.selectedChannelIDs.remove(channelID);
                    } else {
                        channels.selectedChannelIDs.add(channelID);
                    }
                    ((DefaultTreeModel) getModel()).nodeChanged(node);
                }
            }
        }
