    public void operate(AChannelSelection channel1, AChannelSelection channel2) {
        MMArray s1 = channel1.getChannel().getSamples();
        MMArray s2 = channel2.getChannel().getSamples();
        int o1 = channel1.getOffset();
        int l1 = channel1.getLength();
        float ch1, ch2;
        channel1.getChannel().markChange();
        try {
            LProgressViewer.getInstance().entrySubProgress(0.7);
            for (int i = 0; i < l1; i++) {
                if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
                ch1 = s1.get(i + o1);
                ch2 = s2.get(i + o1);
                ch1 = ch1 * r - ch2 * l;
                s1.set(i + o1, channel1.mixIntensity(i + o1, s1.get(i + o1), ch1));
            }
            LProgressViewer.getInstance().exitSubProgress();
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
    }
