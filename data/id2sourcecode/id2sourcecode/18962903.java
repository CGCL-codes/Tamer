    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        int noiseStart = o1;
        int noiseEnd = o1 + l1;
        for (int i = o1; i < o1 + l1; i++) {
            if (Math.abs(s1.get(i)) > silenceLimit) {
                noiseStart = i;
                break;
            }
        }
        for (int i = o1 + l1 - 1; i >= o1; i--) {
            if (Math.abs(s1.get(i)) > silenceLimit) {
                noiseEnd = i;
                break;
            }
        }
        int l = 0;
        if ((noiseEnd - noiseStart) > 0) l = noiseEnd - noiseStart + 1;
        if (l < 2) l = 2;
        MMArray tmp = new MMArray(l, 0);
        try {
            tmp.copy(s1, noiseStart, 0, l);
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
        ch1.getChannel().setSamples(tmp);
    }
