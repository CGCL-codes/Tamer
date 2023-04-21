    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        MMArray tmp = new MMArray(l1, 0);
        ch1.getChannel().markChange();
        LProgressViewer.getInstance().entrySubProgress(0.7);
        for (int i = 0; i < l1; i++) {
            if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
            for (int j = 0; j < delayShape.getLength(); j++) {
                int d = (int) (delay * delayShape.get(j));
                float g = gain * gainShape.get(j);
                if (continueWet || i + d < l1) {
                    tmp.set(i + d, tmp.get(i + d) + s1.get(i + o1) * g);
                }
            }
        }
        LProgressViewer.getInstance().exitSubProgress();
        for (int i = 0; i < tmp.getLength(); i++) {
            float s = dry * s1.get(i + o1) + wet * tmp.get(i);
            s1.set(i + o1, ch1.mixIntensity(i + o1, s1.get(i + o1), s));
        }
        AOToolkit.applyZeroCross(s1, o1);
        AOToolkit.applyZeroCross(s1, o1 + tmp.getLength());
    }
