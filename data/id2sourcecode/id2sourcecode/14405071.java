        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            if (!(node.getUserObject() instanceof TVChannelsSet.Channel)) {
                return this;
            }
            currentChannel = (TVChannelsSet.Channel) node.getUserObject();
            setEnabled(tree.isEnabled());
            if (currentChannel != null) {
                setText(currentChannel.getDisplayName());
                setSelected(channels.selectedChannelIDs.contains(currentChannel.getChannelID()));
            } else {
                setSelected(false);
                setEnabled(false);
            }
            setFont(tree.getFont());
            return this;
        }
