    public void addGroup(DataGroup g) {
        ((DefaultMutableTreeNode) root).add(g);
        int i = getGroupsSize() - 1;
        setLimits(i);
        for (int j = 1; j < getChannelsSize(i); j++) {
            decorateChannel(g, getChannel(i, j));
        }
        int[] ind = new int[1];
        ind[0] = getGroupsSize() - 1;
        nodesWereInserted((DefaultMutableTreeNode) root, ind);
        ind = new int[1];
        ind[0] = getChannelsSize(i) - 1;
        nodesWereInserted((DefaultMutableTreeNode) root.getChildAt(i), ind);
    }
