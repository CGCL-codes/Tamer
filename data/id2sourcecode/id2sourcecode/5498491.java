    public DataChannel getChannel(int g, int c) throws ClassCastException {
        return ((DataGroup) root.getChildAt(g)).getChannel(c);
    }
