    public void operate(AChannelSelection ch1, AChannelSelection ch2) {
        MMArray s1 = ch1.getChannel().getSamples();
        MMArray s2 = ch2.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int o2 = ch2.getOffset();
        int l2 = ch2.getLength();
        ch1.getChannel().markChange();
        MMArray tmp = new MMArray(s1.getLength() + l2, 0);
        tmp.copy(s1, 0, 0, o1);
        tmp.copy(s2, o2, o1, l2);
        tmp.copy(s1, o1, o1 + l2, tmp.getLength() - o1 - l2);
        AOToolkit.applyZeroCross(tmp, o1);
        AOToolkit.applyZeroCross(tmp, o1 + l2);
        ch1.getChannel().setSamples(tmp);
    }
