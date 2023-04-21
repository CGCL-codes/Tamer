    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        double resampledLength = 0;
        for (int i = 0; i < l1; i++) {
            double f = (double) i / (double) l1;
            resampledLength += (1 / beginFactor * (1 - f)) + (1 / endFactor * f);
        }
        MMArray tmp = new MMArray(s1.getLength() - l1 + (int) resampledLength, 0);
        tmp.copy(s1, 0, 0, o1);
        double oldIndex = o1;
        int end = o1 + (int) resampledLength;
        LProgressViewer.getInstance().entrySubProgress(0.7);
        for (int i = o1; i < end; i++) {
            if (LProgressViewer.getInstance().setProgress((i + 1 - o1) * 1.0 / (end - o1))) return;
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
            } else {
                break;
            }
            double f = (double) (i - o1) / resampledLength;
            oldIndex += 1 / (1 / beginFactor * (1 - f) + 1 / endFactor * f);
        }
        LProgressViewer.getInstance().exitSubProgress();
        tmp.copy(s1, o1 + l1, o1 + (int) resampledLength, tmp.getLength() - o1 - (int) resampledLength);
        ch1.getChannel().setSamples(tmp);
    }
