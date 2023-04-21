        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            if (value instanceof ChannelTreeNode<?>) {
                ChannelTreeNode<?> node = (ChannelTreeNode<?>) value;
                setIcon(node.getIcon());
                setText(node.getChannel().getName());
            }
            return this;
        }
