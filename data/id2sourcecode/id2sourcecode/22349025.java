    public void operate(AChannelSelection ch1) {
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        AChannelSelection ch2 = new AChannelSelection(ch1.getChannel(), o1, borderWidth);
        ch2.operateChannel(new AOFade(AOFade.IN, order, 0, false));
        AChannelSelection ch3 = new AChannelSelection(ch1.getChannel(), o1 + l1 - borderWidth, borderWidth);
        ch3.operateChannel(new AOFade(AOFade.OUT, order, 0, false));
    }
