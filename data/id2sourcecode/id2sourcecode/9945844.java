    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        int resampledLength = (int) ((float) l1 / sampleRateFactor);
        ch1.getChannel().markChange();
        MMArray tmp = new MMArray(s1.getLength() - l1 + resampledLength, 0);
        tmp.copy(s1, 0, 0, o1);
        double oldIndex = o1;
        LProgressViewer.getInstance().entrySubProgress(0.7);
        for (int i = o1; i < o1 + resampledLength; i++) {
            if (LProgressViewer.getInstance().setProgress((i + 1 - o1) * 1.0 / (int) resampledLength)) return;
            if (((int) oldIndex + 1) < s1.getLength()) {
                switch(order) {
                    case 0:
                        tmp.set(i, AOToolkit.interpolate0(s1, (float) oldIndex));
                        break;
                    case 1:
                        tmp.set(i, AOToolkit.interpolate1(s1, (float) oldIndex));
                        break;
                    case 2:
                        tmp.set(i, AOToolkit.interpolate2(s1, (float) oldIndex));
                        break;
                    case 3:
                        tmp.set(i, AOToolkit.interpolate3(s1, (float) oldIndex));
                        break;
                }
            } else break;
            oldIndex += sampleRateFactor;
        }
        LProgressViewer.getInstance().exitSubProgress();
        tmp.copy(s1, o1 + l1, o1 + resampledLength, tmp.getLength() - o1 - resampledLength);
        ch1.getChannel().setSamples(tmp);
    }
