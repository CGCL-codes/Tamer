    public final void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        try {
            LProgressViewer.getInstance().entrySubProgress(0.7);
            for (int i = 0; i < l1; i++) {
                if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
                int h = Math.abs((int) (s1.get(o1 + i)));
                if (h >= histogram.getLength()) {
                    h = histogram.getLength() - 1;
                } else if (h < 0) {
                    h = 0;
                }
                if (Math.abs((int) (s1.get(o1 + i - 1))) < h && Math.abs((int) (s1.get(o1 + i + 1))) < h) {
                    histogram.set(h, histogram.get(h) + 1);
                }
            }
            LProgressViewer.getInstance().exitSubProgress();
        } catch (ArrayIndexOutOfBoundsException oob) {
            Debug.printStackTrace(5, oob);
        }
    }
