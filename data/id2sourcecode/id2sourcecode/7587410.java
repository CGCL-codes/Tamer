    public Vector getChannelQue(int i) {
        if ((i >= 0) && (i < channelQue.size())) return (Vector) channelQue.elementAt(i);
        return new Vector();
    }
