    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        int sampleWidth = 1 << ch1.getChannel().getParentClip().getSampleWidth();
        int minDerivation = (int) (minDerivationFactor * sampleWidth);
        try {
            LProgressViewer.getInstance().entrySubProgress(0.7);
            for (int i = 1; i < l1; i++) {
                if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
                float dBegin = s1.get(o1 + i) - s1.get(o1 + i - 1);
                float dEnd;
                if (Math.abs(dBegin) > minDerivation) {
                    for (int j = 1; j < maxWidth; j++) {
                        dEnd = s1.get(o1 + i + j) - s1.get(o1 + i + j - 1);
                        if (Math.abs(dEnd) > minDerivation) {
                            if ((dBegin > 0) && (dEnd < 0)) {
                                for (int k = 0; k < j; k++) {
                                    s1.set(o1 + i + k, s1.get(o1 + i + k) - sampleWidth);
                                }
                            } else if ((dBegin < 0) && (dEnd > 0)) {
                                for (int k = 0; k < j; k++) {
                                    s1.set(o1 + i + k, s1.get(o1 + i + k) + sampleWidth);
                                }
                            }
                            i += j + 1;
                            break;
                        }
                    }
                }
            }
            LProgressViewer.getInstance().exitSubProgress();
        } catch (ArrayIndexOutOfBoundsException oob) {
            Debug.printStackTrace(5, oob);
        }
    }
