    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        AOFifo delayFifo = new AOFifo((int) (baseDelay + modulationDelay + 10));
        LProgressViewer.getInstance().entrySubProgress(0.7);
        for (int i = o1; i < (o1 + l1); i++) {
            if (LProgressViewer.getInstance().setProgress((i + 1 - o1) * 1.0 / l1)) return;
            float iMod = baseDelay;
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
            float s = s1.get(i);
            if ((int) iMod < delayFifo.getActualSize() - 1) {
                delayFifo.put(s + (delayFifo.pickFromHead(iMod - 1) * feedback));
                if (negFeedback) {
                    s = (s * dry) - (delayFifo.pickFromHead(iMod) * wet);
                } else {
                    s = (s * dry) + (delayFifo.pickFromHead(iMod) * wet);
                }
            } else {
                delayFifo.put(s);
                s = s * dry;
            }
            s1.set(i, ch1.mixIntensity(i, s1.get(i), s));
        }
        AOToolkit.applyZeroCross(s1, o1);
        AOToolkit.applyZeroCross(s1, o1 + l1);
        LProgressViewer.getInstance().exitSubProgress();
    }
