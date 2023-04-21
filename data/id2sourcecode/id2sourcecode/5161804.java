    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        MMArray tmp = new MMArray(l1, 0);
        ch1.getChannel().markChange();
        LProgressViewer.getInstance().entrySubProgress(0.7);
        for (int i = 0; i < l1; i++) {
            if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
            tmp.set(i, AOToolkit.convolve(s1, i + o1, kernel, kernel.getLength()));
        }
        LProgressViewer.getInstance().exitSubProgress();
        for (int i = 0; i < l1; i++) {
            s1.set(i + o1, ch1.mixIntensity(i + o1, s1.get(i + o1), tmp.get(i)));
        }
        AOToolkit.applyZeroCross(s1, o1);
        AOToolkit.applyZeroCross(s1, o1 + l1);
    }
