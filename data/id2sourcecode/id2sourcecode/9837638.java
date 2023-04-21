    public void operate(AChannelSelection ch1, AChannelSelection ch2) {
        MMArray s1 = ch1.getChannel().getSamples();
        MMArray s2 = ch2.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        int o2 = ch2.getOffset();
        int l2 = ch2.getLength();
        int l = Math.min(l1, l2);
        ch1.getChannel().markChange();
        for (int i = 0; i < l; i++) {
            s1.set(i + o1, ch1.mixIntensity(i + o1, s1.get(i + o1), s2.get(i + o2)));
        }
        AOToolkit.applyZeroCross(s1, o1);
        AOToolkit.applyZeroCross(s1, o1 + l);
    }
