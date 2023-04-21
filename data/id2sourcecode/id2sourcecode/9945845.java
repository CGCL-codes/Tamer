    public void operate(AChannelSelection ch1, AChannelSelection ch2) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        MMArray s2 = ch2.getChannel().getSamples();
        ch1.getChannel().markChange();
        double resampledLength = 0;
        for (int i = 0; i < l1; i++) {
            try {
                resampledLength += 1 / s2.get(o1 + i);
            } catch (ArrayIndexOutOfBoundsException aoobe) {
                resampledLength += 1;
            }
        }
        MMArray tmp = new MMArray(s1.getLength() - l1 + (int) resampledLength, 0);
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
            try {
                if (s2.get((int) oldIndex) > 0) oldIndex += s2.get((int) oldIndex); else oldIndex += 1;
            } catch (ArrayIndexOutOfBoundsException aoobe) {
                oldIndex += 1;
            }
        }
        LProgressViewer.getInstance().exitSubProgress();
        tmp.copy(s1, o1 + l1, o1 + (int) resampledLength, tmp.getLength() - o1 - (int) resampledLength);
        ch1.getChannel().setSamples(tmp);
    }
