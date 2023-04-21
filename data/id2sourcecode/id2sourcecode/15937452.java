    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        try {
            MMArray d = new MMArray(l1, 0);
            d.copy(s1, o1, 0, d.getLength());
            AOToolkit.derivate(d, 0, d.getLength());
            AOToolkit.derivate(d, 0, d.getLength());
            AOToolkit.derivate(d, 0, d.getLength());
            AOToolkit.derivate(d, 0, d.getLength());
            float max = 0;
            for (int i = 0; i < d.getLength(); i++) {
                if (Math.abs(d.get(i)) > max) {
                    max = Math.abs(d.get(i));
                }
            }
            float minClickPeriod = 200;
            float oldX = 0;
            LProgressViewer.getInstance().entrySubProgress(0.7);
            for (int i = 0; i < d.getLength(); i++) {
                if (d.get(i) > max * sense) {
                    if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / d.getLength())) return;
                    if ((i - oldX) > minClickPeriod) {
                        AOToolkit.applyZeroCross(s1, i + o1, smooth);
                    }
                    oldX = i;
                }
            }
            LProgressViewer.getInstance().exitSubProgress();
        } catch (ArrayIndexOutOfBoundsException oob) {
            Debug.printStackTrace(5, oob);
        }
    }
