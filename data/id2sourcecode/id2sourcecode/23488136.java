    public void operate(AChannelSelection channel1, AChannelSelection channel2, AChannelSelection param) {
        MMArray p = param.getChannel().getSamples();
        int o1 = channel1.getOffset();
        int l1 = channel1.getLength();
        if (modifyCh1) {
            channel1.getChannel().markChange();
        }
        if (modifyCh2) {
            channel2.getChannel().markChange();
        }
        try {
            LProgressViewer.getInstance().entrySubProgress(0.7);
            for (int i = 0; i < l1; i++) {
                if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
                float wide = p.get(i + o1);
                if (wide < 1) {
                    narrowing(channel1, channel2, o1 + i, 1 - wide);
                } else {
                    widening(channel1, channel2, o1 + i, wide - 1);
                }
            }
            LProgressViewer.getInstance().exitSubProgress();
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
    }
