    public void MagOff(String initPvStr, int val) {
        String chName = initPvStr + ":Cmd_Stop";
        Channel ch = ChannelFactory.defaultFactory().getChannel(chName);
        setValue(ch, val);
    }
