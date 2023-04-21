    public void operate(AChannelSelection ch1, AChannelSelection ch2) {
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        if (modifyCh1) {
            ch1.getChannel().markChange();
        }
        if (modifyCh2) {
            ch2.getChannel().markChange();
        }
        try {
            LProgressViewer.getInstance().entrySubProgress(0.7);
            for (int i = 0; i < l1; i++) {
                if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
                if (wide < 1) {
                    narrowing(ch1, ch2, o1 + i, 1 - wide);
                } else {
                    widening(ch1, ch2, o1 + i, wide - 1);
                }
            }
            LProgressViewer.getInstance().exitSubProgress();
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
    }
