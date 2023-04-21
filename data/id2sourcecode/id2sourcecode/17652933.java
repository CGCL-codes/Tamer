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
            if (continueBefore) {
                for (int i = 0; i < o1; i++) {
                    float wide = wideBegin;
                    if (wide < 1) {
                        narrowing(ch1, ch2, i, 1 - wide);
                    } else {
                        widening(ch1, ch2, i, wide - 1);
                    }
                }
            }
            for (int i = 0; i < l1; i++) {
                if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
                float wide = wideBegin + ((wideEnd - wideBegin) * i / l1);
                if (wide < 1) {
                    narrowing(ch1, ch2, o1 + i, 1 - wide);
                } else {
                    widening(ch1, ch2, o1 + i, wide - 1);
                }
            }
            if (continueAfter) {
                int length = ch1.getChannel().getSamples().getLength();
                for (int i = o1 + l1; i < length; i++) {
                    float wide = wideEnd;
                    if (wide < 1) {
                        narrowing(ch1, ch2, i, 1 - wide);
                    } else {
                        widening(ch1, ch2, i, wide - 1);
                    }
                }
            }
            LProgressViewer.getInstance().exitSubProgress();
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
    }
