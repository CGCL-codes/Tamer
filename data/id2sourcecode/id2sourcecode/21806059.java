    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        int ns = o1;
        int i = o1;
        int ss = 0;
        final int NOISE = 1;
        final int BEGIN_OF_SILENCE = 2;
        final int SILENCE = 3;
        int state = NOISE;
        try {
            while (i < o1 + l1) {
                switch(state) {
                    case NOISE:
                        s1.set(ns++, s1.get(i));
                        if (Math.abs(s1.get(i)) <= silenceLimit) {
                            ss = 1;
                            state = BEGIN_OF_SILENCE;
                        }
                        break;
                    case BEGIN_OF_SILENCE:
                        s1.set(ns++, s1.get(i));
                        if (Math.abs(s1.get(i)) <= silenceLimit) {
                            if (ss++ >= tMinSilence) {
                                state = SILENCE;
                            }
                        } else {
                            state = NOISE;
                        }
                        break;
                    case SILENCE:
                        if (Math.abs(s1.get(i)) > silenceLimit) {
                            s1.set(ns++, s1.get(i));
                            state = NOISE;
                        }
                        break;
                }
                i++;
            }
            MMArray tmp = new MMArray(s1.getLength() + ns - 1 - l1, 0);
            tmp.copy(s1, 0, 0, o1);
            tmp.copy(s1, o1, o1, ns - o1);
            tmp.copy(s1, ns, ns, tmp.getLength() - ns);
            ch1.getChannel().setSamples(tmp);
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
    }
