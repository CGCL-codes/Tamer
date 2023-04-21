    public Position getChannel(int num) {
        if (_chans.containsKey(new Integer(num))) return _chans.get(new Integer(num)); else return new Position();
    }
