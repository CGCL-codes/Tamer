    public int getChannelFor(int d) {
        for (int x = 0; x < _totalChannels; x++) if (_patch.get(x).contains(new Integer(d))) return x;
        return -1;
    }
