    public void actionPerformed(ActionEvent e) {
        TreePath[] treepaths = ChannelEditor.application.getChannelListingPanel().getSelectionPaths();
        if (treepaths != null) {
            for (int i = 0; i < treepaths.length; i++) {
                TreePath path = treepaths[i];
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                if (!node.isRoot()) {
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) node.getParent();
                    node.removeFromParent();
                    ChannelEditor.application.getChannelDeletedPanel().addElement(node);
                    ChannelEditor.application.getChannelListingPanel().treeNodeStructureChanged(parentNode);
                }
            }
            ChannelEditor.application.setModified(true);
        }
    }
