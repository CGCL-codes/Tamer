    public void operate(AChannelSelection ch1, AChannelSelection ch2) {
        AChannel s1 = ch1.getChannel();
        AChannel s2 = ch2.getChannel();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        int o2 = ch2.getOffset();
        ch1.getChannel().markChange();
        s1.getMask().prepareResults();
        s2.getMask().prepareResults();
        if (ch2.getChannel().isAudible()) {
            try {
                for (int i = 0; i < l1; i++) {
                    s1.setSample(o1 + i, s1.getMaskedSample(o1 + i) * volume1 + s2.getMaskedSample(o2 + i) * volume2);
                }
            } catch (ArrayIndexOutOfBoundsException oob) {
            }
        }
    }
