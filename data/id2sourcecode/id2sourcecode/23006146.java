    public void operate(AChannelSelection ch1) {
        MMArray sample = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        float oldRms = AOToolkit.rmsAverage(sample, o1, l1);
        MMArray tmp = new MMArray(l1, 0);
        ch1.getChannel().markChange();
        try {
            if (backward) {
                for (int i = 0; i < l1; i++) {
                    tmp.set(l1 - 1 - i, sample.get(i + o1));
                }
            } else {
                tmp.copy(sample, o1, 0, l1);
            }
            for (int i = 0; i < l1; i++) {
                if (i + delay < l1) {
                    if (negFeedback) {
                        tmp.set(i + delay, tmp.get(i + delay) - tmp.get(i) * gain);
                    } else {
                        tmp.set(i + delay, tmp.get(i + delay) + tmp.get(i) * gain);
                    }
                }
            }
            float newRms = AOToolkit.rmsAverage(tmp, 0, l1);
            AOToolkit.multiply(tmp, 0, l1, (float) (oldRms / newRms));
            if (backward) {
                for (int i = o1; i < o1 + l1; i++) {
                    float s = sample.get(i) * dry + tmp.get(l1 - 1 - i - o1) * wet;
                    sample.set(i, ch1.mixIntensity(i, sample.get(i), s));
                }
            } else {
                for (int i = o1; i < o1 + l1; i++) {
                    float s = sample.get(i) * dry + tmp.get(i - o1) * wet;
                    sample.set(i, ch1.mixIntensity(i, sample.get(i), s));
                }
            }
            AOToolkit.applyZeroCross(sample, o1);
            AOToolkit.applyZeroCross(sample, o1 + l1);
        } catch (ArrayIndexOutOfBoundsException oob) {
            Debug.printStackTrace(5, oob);
        }
    }
