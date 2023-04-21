    public void resetMag(String initPvStr, int val) {
        String chName = initPvStr + ":Reset";
        Channel ch = ChannelFactory.defaultFactory().getChannel(chName);
        setValue(ch, val);
    }
