    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        float a;
        switch(mode) {
            case IN:
                try {
                    LProgressViewer.getInstance().entrySubProgress(0.7);
                    for (int i = 0; i < l1; i++) {
                        if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
                        a = (float) i / (float) l1;
                        a = performOrder(a);
                        s1.set(o1 + i, s1.get(o1 + i) * (a * variableFactor + lowFactor));
                    }
                    LProgressViewer.getInstance().exitSubProgress();
                    if (continueLow) {
                        for (int i = 0; i < o1; i++) {
                            s1.set(i, s1.get(i) * lowFactor);
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException oob) {
                }
                break;
            case OUT:
                try {
                    LProgressViewer.getInstance().entrySubProgress(0.7);
                    for (int i = 0; i < l1; i++) {
                        if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
                        a = (float) (l1 - i) / (float) l1;
                        a = performOrder(a);
                        s1.set(o1 + i, s1.get(o1 + i) * (a * variableFactor + lowFactor));
                    }
                    LProgressViewer.getInstance().exitSubProgress();
                    if (continueLow) {
                        for (int i = o1 + l1; i < s1.getLength(); i++) {
                            s1.set(i, s1.get(i) * lowFactor);
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException oob) {
                }
                break;
            case CROSS:
                int lh = l1 / 2;
                float b;
                MMArray tmp = new MMArray(s1.getLength() - lh, 0);
                try {
                    LProgressViewer.getInstance().entrySubProgress(0.7);
                    for (int i = 0; i < lh; i++) {
                        if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / lh)) return;
                        a = (float) i / (float) lh;
                        a = performOrder(a);
                        b = (float) (lh - i) / (float) lh;
                        b = performOrder(b);
                        s1.set(o1 + i, b * s1.get(o1 + i) + a * s1.get(o1 + lh + i));
                    }
                    LProgressViewer.getInstance().exitSubProgress();
                    tmp.copy(s1, 0, 0, o1 + lh);
                    tmp.copy(s1, o1 + l1, o1 + l1 - lh, s1.getLength() - o1 - l1);
                } catch (ArrayIndexOutOfBoundsException oob) {
                }
                ch1.getChannel().setSamples(tmp);
                break;
        }
    }
