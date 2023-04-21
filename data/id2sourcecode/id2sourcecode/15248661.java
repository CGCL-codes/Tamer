    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        LProgressViewer.getInstance().entrySubProgress(0.7);
        switch(type) {
            case CLAMPING_TYPE:
                for (int i = o1; i < (o1 + l1); i++) {
                    float s = s1.get(i);
                    if (LProgressViewer.getInstance().setProgress((i + 1 - o1) * 1.0 / l1)) return;
                    if (s1.get(i) > threshold) s = clamping; else if (s1.get(i) < -threshold) s = -clamping;
                    s1.set(i, ch1.mixIntensity(i, s1.get(i), s));
                }
                break;
            case NOISE_GATING_TYPE:
                for (int i = o1; i < (o1 + l1); i++) {
                    float s = s1.get(i);
                    if (LProgressViewer.getInstance().setProgress((i + 1 - o1) * 1.0 / l1)) return;
                    if ((s1.get(i) < threshold) && (s1.get(i) > 0)) s = clamping; else if ((s1.get(i) > -threshold) && (s1.get(i) < 0)) s = -clamping;
                    s1.set(i, ch1.mixIntensity(i, s1.get(i), s));
                }
                break;
        }
        LProgressViewer.getInstance().exitSubProgress();
    }
