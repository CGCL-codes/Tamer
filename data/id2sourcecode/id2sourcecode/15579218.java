    private boolean sameTree(JTree tree) {
        TreeModel m = tree.getModel();
        ChannelTree ct = getChannelTree();
        if (ct == null) return false;
        return sameTreeNodes(ct.rootIterator(), (DefaultMutableTreeNode) m.getRoot());
    }
