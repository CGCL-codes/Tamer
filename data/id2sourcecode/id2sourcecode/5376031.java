    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        MMArray tmp = new MMArray(l1, 0);
        float oldRms = AOToolkit.rmsAverage(s1, o1, l1);
        tmp.copy(s1, o1, 0, l1);
        ch1.getChannel().markChange();
        AOToolkit.setIirHighPass(tmp, 0, l1, dry, wet, freq);
        float newRms = AOToolkit.rmsAverage(tmp, 0, l1);
        AOToolkit.multiply(tmp, 0, l1, (float) (oldRms / newRms));
        for (int i = 0; i < l1; i++) {
            s1.set(i + o1, ch1.mixIntensity(i + o1, s1.get(i + o1), tmp.get(i)));
        }
        AOToolkit.applyZeroCross(s1, o1);
        AOToolkit.applyZeroCross(s1, o1 + l1);
    }
