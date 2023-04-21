    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        MMArray tmp = new MMArray(l1, 0);
        ch1.getChannel().markChange();
        tmp.copy(s1, o1, 0, l1);
        float oldRms = AOToolkit.rmsAverage(tmp, 0, tmp.getLength());
        AChannelSelection ch2 = new AChannelSelection(ch1.getChannel(), 0, o1);
        ch2.operateChannel(new AOFade(AOFade.IN, order, 0, false));
        AChannelSelection ch3 = new AChannelSelection(ch1.getChannel(), o1 + l1, s1.getLength() - o1 - l1);
        ch3.operateChannel(new AOFade(AOFade.OUT, order, 0, false));
        for (int i = 0; i < l1; i++) {
            if (o1 - l1 + i >= 0) {
                tmp.set(i, tmp.get(i) + s1.get(o1 - l1 + i));
            }
        }
        for (int i = 0; i < l1; i++) {
            if (o1 + l1 + i < s1.getLength()) {
                tmp.set(i, tmp.get(i) + s1.get(o1 + l1 + i));
            }
        }
        float newRms = AOToolkit.rmsAverage(tmp, 0, tmp.getLength());
        AOToolkit.multiply(tmp, 0, tmp.getLength(), (float) (oldRms / newRms));
        ch1.getChannel().setSamples(tmp);
    }
