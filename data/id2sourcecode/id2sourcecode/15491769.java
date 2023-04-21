    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        MMArray tmp1, tmp2;
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        if ((newIndex >= o1) && (newIndex < o1 + l1)) {
            return;
        }
        int ni = newIndex;
        if (ni < 0) {
            ni = 0;
        } else if (ni > o1 + l1) {
            ni -= l1;
        }
        tmp1 = new MMArray(s1.getLength() - l1, 0);
        tmp1.copy(s1, 0, 0, o1);
        tmp1.copy(s1, o1 + l1, o1, s1.getLength() - o1 - l1);
        tmp2 = new MMArray(s1.getLength(), 0);
        tmp2.copy(tmp1, 0, 0, ni);
        tmp2.copy(s1, o1, ni, l1);
        tmp2.copy(tmp1, ni, ni + l1, tmp1.getLength() - ni);
        AOToolkit.applyZeroCross(tmp2, ni);
        AOToolkit.applyZeroCross(tmp2, ni + l1);
        tmp2.cleanup();
        ch1.getChannel().setSamples(tmp2);
    }
