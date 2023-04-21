    public void MagOn(String initPvStr, int val) {
        String chName = initPvStr + ":Cmd_Strt";
        Channel ch = ChannelFactory.defaultFactory().getChannel(chName);
        setValue(ch, val);
    }
