    public void actionPerformed(ActionEvent e) {
        TreePath treePath = ChannelEditor.application.getChannelListingPanel().getLeadSelectionPath();
        if (treePath != null) {
            String namePrefix = JOptionPane.showInputDialog(ChannelEditor.application, Messages.getString("MultiRenameAction.2"), Messages.getString("MultiRenameAction.3"), JOptionPane.QUESTION_MESSAGE);
            if (!Utils.isEmpty(namePrefix)) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
                if (node.isRoot()) {
                    Enumeration enumer = node.children();
                    while (enumer.hasMoreElements()) {
                        doRename((DefaultMutableTreeNode) enumer.nextElement(), namePrefix);
                    }
                    doRename(node, namePrefix);
                } else {
                    doRename(node, namePrefix);
                }
                ChannelEditor.application.getChannelListingPanel().treeNodeStructureChanged(node);
            }
        }
    }
