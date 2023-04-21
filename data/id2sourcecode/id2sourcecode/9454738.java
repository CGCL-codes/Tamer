    public void setLLRFpv(String initPvStr, int val) {
        String chName = initPvStr + ":ChtFlt";
        Channel ch = ChannelFactory.defaultFactory().getChannel(chName);
        setValue(ch, val);
    }
