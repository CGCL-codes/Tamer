    public void actionPerformed(ActionEvent e) {
        TreePath treePath = ChannelEditor.application.getChannelListingPanel().getLeadSelectionPath();
        if (treePath != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
            if (node.isRoot()) {
                String categoryName = JOptionPane.showInputDialog(ChannelEditor.application, Messages.getString("CreateCategoryAction.2"), Messages.getString("CreateCategoryAction.3"), JOptionPane.QUESTION_MESSAGE);
                if (!Utils.isEmpty(categoryName)) {
                    categoryName = categoryName.trim().replace(':', '|');
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new ChannelCategory(categoryName));
                    ChannelEditor.application.getChannelListingPanel().insertNodeInto(newNode, node, node.getChildCount());
                    ChannelEditor.application.getChannelListingPanel().treeNodeStructureChanged(node);
                    ChannelEditor.application.setModified(true);
                }
            }
        }
    }
