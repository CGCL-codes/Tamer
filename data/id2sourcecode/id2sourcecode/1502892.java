    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        float oldRms = AOToolkit.rmsAverage(s1, o1, l1);
        ch1.getChannel().markChange();
        meanAmplitude = 1;
        if (backward) {
            for (int i = l1 - 1; i >= Math.max(0, l1 - tAttack - tRelease); i--) {
                updateAmplitude(s1.get(i + o1));
            }
        } else {
            for (int i = 0; i < Math.min(l1, tAttack + tRelease); i++) {
                updateAmplitude(s1.get(i + o1));
            }
        }
        try {
            LProgressViewer.getInstance().entrySubProgress(0.7);
            if (backward) {
                for (int i = l1 - 1; i >= 0; i--) {
                    if (LProgressViewer.getInstance().setProgress((l1 - i) * 1.0 / l1)) return;
                    updateAmplitude(s1.get(i + o1));
                    s1.set(i + o1, ch1.mixIntensity(i + o1, s1.get(i + o1), (1000 / meanAmplitude * s1.get(i + o1)) * wet + s1.get(i + o1) * dry));
                }
            } else {
                for (int i = 0; i < l1; i++) {
                    if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
                    updateAmplitude(s1.get(i + o1));
                    s1.set(i + o1, ch1.mixIntensity(i + o1, s1.get(i + o1), (1000 / meanAmplitude * s1.get(i + o1)) * wet + s1.get(i + o1) * dry));
                }
            }
            float newRms = AOToolkit.rmsAverage(s1, o1, l1);
            AOToolkit.multiply(s1, o1, l1, (float) (oldRms / newRms));
            LProgressViewer.getInstance().exitSubProgress();
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
    }
