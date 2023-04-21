    public void operate(AChannelSelection ch1) {
        MMArray sample = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        MMArray sample2 = new MMArray(l1, 0);
        MMArray sample3 = new MMArray(l1, 0);
        float oldRms = AOToolkit.rmsAverage(sample, o1, l1);
        ch1.getChannel().markChange();
        try {
            LProgressViewer.getInstance().entrySubProgress(0.7);
            for (int k = 0; k < delayShape.getLength(); k++) {
                if (LProgressViewer.getInstance().setProgress((k + 1) * 1.0 / delayShape.getLength())) return;
                if (backward) {
                    for (int i = 0; i < sample2.getLength(); i++) {
                        sample2.set(l1 - 1 - i, sample.get(i + o1));
                    }
                } else {
                    sample2.copy(sample, o1, 0, sample2.getLength());
                }
                int d = (int) (delay * delayShape.get(k));
                float g = gain * gainShape.get(k);
                for (int i = 0; i < l1; i++) {
                    if ((i + d < l1) && (i + d >= 0)) {
                        if (negFeedback) {
                            sample2.set(i + d, sample2.get(i + d) - sample2.get(i) * g);
                        } else {
                            sample2.set(i + d, sample2.get(i + d) + sample2.get(i) * g);
                        }
                    }
                }
                if (backward) {
                    for (int i = 0; i < sample2.getLength(); i++) {
                        sample2.set(l1 - 1 - i, sample2.get(l1 - 1 - i) - sample.get(i + o1));
                    }
                } else {
                    for (int i = 0; i < sample2.getLength(); i++) {
                        sample2.set(i, sample2.get(i) - sample.get(i + o1));
                    }
                }
                for (int i = 0; i < l1; i++) {
                    sample3.set(i, sample3.get(i) + sample2.get(i));
                }
            }
            LProgressViewer.getInstance().exitSubProgress();
            float newRms = AOToolkit.rmsAverage(sample3, 0, l1);
            AOToolkit.multiply(sample3, 0, l1, (float) (oldRms / newRms));
            if (backward) {
                for (int i = 0; i < l1; i++) {
                    float s = dry * sample.get(i + o1) + wet * sample3.get(l1 - 1 - i);
                    sample.set(i + o1, ch1.mixIntensity(i + o1, sample.get(i + o1), s));
                }
            } else {
                for (int i = 0; i < l1; i++) {
                    float s = dry * sample.get(i + o1) + wet * sample3.get(i);
                    sample.set(i + o1, ch1.mixIntensity(i + o1, sample.get(i + o1), s));
                }
            }
        } catch (ArrayIndexOutOfBoundsException oob) {
            Debug.printStackTrace(5, oob);
        }
    }
