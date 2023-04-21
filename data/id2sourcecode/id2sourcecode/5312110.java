    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        float currentMaxValue = 0;
        ch1.getChannel().markChange();
        switch(mode) {
            case PEAK:
                currentMaxValue = AOToolkit.max(s1, o1, l1);
                break;
            case RMS:
                currentMaxValue = AOToolkit.rmsAverage(s1, o1, l1);
                break;
        }
        float scale = maxValue / currentMaxValue;
        LProgressViewer.getInstance().entrySubProgress(0.7);
        for (int i = o1; i < (o1 + l1); i++) {
            if (LProgressViewer.getInstance().setProgress((i + 1 - o1) * 1.0 / l1)) return;
            s1.set(i, ch1.mixIntensity(i, s1.get(i), s1.get(i) * scale));
        }
        LProgressViewer.getInstance().exitSubProgress();
    }
