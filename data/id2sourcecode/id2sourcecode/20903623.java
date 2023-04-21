    public void actionPerformed(ActionEvent e) {
        TreePath treePath = ChannelEditor.application.getChannelListingPanel().getLeadSelectionPath();
        if (treePath != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            if (node.isRoot()) {
                Enumeration enumer = node.children();
                while (enumer.hasMoreElements()) {
                    doSorting((DefaultMutableTreeNode) enumer.nextElement());
                }
                doSorting(node);
            } else {
                doSorting(node);
            }
            ChannelEditor.application.getChannelListingPanel().treeNodeStructureChanged(node);
        }
    }
