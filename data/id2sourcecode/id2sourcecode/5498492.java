    public DefaultMutableTreeNode getChannelNode(int g, int c) throws ClassCastException {
        return (DefaultMutableTreeNode) root.getChildAt(g).getChildAt(c);
    }
