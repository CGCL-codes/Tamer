    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        MMArray tmp = new MMArray(l1, 0);
        ch1.getChannel().markChange();
        LProgressViewer.getInstance().entrySubProgress(0.7);
        for (int i = 0; i < l1; i++) {
            if (LProgressViewer.getInstance().setProgress((i + 1) * 1.0 / l1)) return;
            float iMod = 0;
            switch(modulationShape) {
                case SINUS:
                    iMod += (float) Math.sin((float) (i % (int) modulationPeriod) / modulationPeriod * 2 * Math.PI) * modulationDelay;
                    break;
                case TRIANGLE:
                    iMod += (Math.abs(((float) (i % (int) modulationPeriod) / modulationPeriod * 4) - 2) - 1) * modulationDelay;
                    break;
                case SAW:
                    iMod += (((float) (i % (int) modulationPeriod) / modulationPeriod * 2) - 1) * modulationDelay;
                    break;
            }
            double m = o1 + i + iMod;
            if (m < 0) m = 0; else if (m >= s1.getLength()) m = s1.getLength() - 1;
            tmp.set(i, AOToolkit.interpolate0(s1, (float) m));
        }
        LProgressViewer.getInstance().exitSubProgress();
        for (int i = 0; i < tmp.getLength(); i++) {
            s1.set(i + o1, ch1.mixIntensity(i + o1, s1.get(i + o1), tmp.get(i)));
        }
    }
