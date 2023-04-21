    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        int l2 = l1 / voices;
        MMArray tmp = new MMArray(s1.getLength() - l1 + l2, 0);
        ch1.getChannel().markChange();
        float oldRms = AOToolkit.rmsAverage(s1, o1, l1);
        tmp.copy(s1, 0, 0, o1);
        for (int i = o1; i < l1; i++) {
            tmp.set(o1 + (i % l2), tmp.get(o1 + (i % l2)) + s1.get(i));
        }
        tmp.copy(s1, o1 + l1, o1 + l2, s1.getLength() - o1 - l1);
        float newRms = AOToolkit.rmsAverage(tmp, o1, l2);
        AOToolkit.multiply(tmp, 0, tmp.getLength(), (float) (oldRms / newRms));
        AOToolkit.applyZeroCross(tmp, o1);
        AOToolkit.applyZeroCross(tmp, o1 + l2);
        ch1.getChannel().setSamples(tmp);
    }
