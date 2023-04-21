    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        initAmplitude();
        try {
            LProgressViewer.getInstance().entrySubProgress(0.7);
            for (int i = o1; i < o1 + l1; i++) {
                if (LProgressViewer.getInstance().setProgress((i + 1 - o1) * 1.0 / l1)) return;
                updateAmplitude(s1.get(i));
                float f = AOToolkit.interpolate1(fx, fy, meanAmplitude);
                s1.set(i, ch1.mixIntensity(i, s1.get(i), s1.get(i) * f));
            }
            LProgressViewer.getInstance().exitSubProgress();
            AOToolkit.applyZeroCross(s1, o1);
            AOToolkit.applyZeroCross(s1, o1 + l1);
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
    }
