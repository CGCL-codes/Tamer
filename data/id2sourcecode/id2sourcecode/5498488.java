    public void addChannel(DataChannel c, int g) {
        ((DataGroup) root.getChildAt(g)).addChannel(c);
        decorateChannel((DataGroup) root.getChildAt(g), c);
        setLimits(g);
        int[] ind = new int[1];
        ind[0] = getChannelsSize(g) - 1;
        nodesWereInserted((DefaultMutableTreeNode) root.getChildAt(g), ind);
    }
