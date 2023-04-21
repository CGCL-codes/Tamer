    public void actionPerformed(ActionEvent e) {
        TreePath treePath = ChannelEditor.application.getChannelListingPanel().getLeadSelectionPath();
        DefaultMutableTreeNode node = null;
        int insertPosition = 0;
        if (treePath == null) {
            node = ChannelEditor.application.getChannelListingPanel().getRootNode();
        } else {
            node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
        }
        if (!node.isRoot() && !((ChannelElement) node.getUserObject()).isCategory()) {
            DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
            insertPosition = parent.getIndex(node);
            node = parent;
        }
        CreateChannelDialog createChannelDlg = new CreateChannelDialog(ChannelEditor.application);
        int result = createChannelDlg.showDialog();
        if (result == CreateChannelDialog.RESULT_CREATE) {
            Channel channel = createChannelDlg.getChannel();
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(channel);
            ChannelEditor.application.getChannelListingPanel().insertNodeInto(newNode, node, insertPosition);
            ChannelEditor.application.getChannelListingPanel().treeNodeStructureChanged(node);
            ChannelEditor.application.setModified(true);
        }
    }
