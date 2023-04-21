    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        float oldRms = AOToolkit.rmsAverage(s1, o1, l1);
        MMArray tmp = new MMArray(l1, 0);
        ch1.getChannel().markChange();
        AOToolkit.addIirBandPass(s1, tmp, o1, l1, freq, q, wet);
        float newRms = AOToolkit.rmsAverage(tmp, 0, l1);
        AOToolkit.multiply(tmp, 0, l1, (float) (oldRms / newRms));
        for (int i = 0; i < l1; i++) {
            s1.set(i + o1, ch1.mixIntensity(i + o1, s1.get(i + o1), s1.get(i + o1) * dry + tmp.get(i)));
        }
        AOToolkit.applyZeroCross(s1, o1);
        AOToolkit.applyZeroCross(s1, o1 + l1);
    }
