    public void operate(AChannelSelection ch1, AChannelSelection ch2) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        MMArray f = ch2.getChannel().getSamples();
        ch1.getChannel().markChange();
        LProgressViewer.getInstance().entrySubProgress(0.7);
        float s;
        for (int i = 0; i < l1; i++) {
            if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
            float in = s1.get(i + o1);
            if (in >= 0) {
                s = AOToolkit.interpolate3(f, in);
            } else {
                s = -AOToolkit.interpolate3(f, -in);
            }
            s1.set(i + o1, ch1.mixIntensity(i + o1, s1.get(i + o1), s));
        }
        LProgressViewer.getInstance().exitSubProgress();
    }
