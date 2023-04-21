    public DataChannel getChannel(int c) throws ClassCastException {
        return (DataChannel) ((DefaultMutableTreeNode) this.getChildAt(c)).getUserObject();
    }
