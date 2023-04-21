    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        MMArray tmp = new MMArray(period, 0);
        for (int i = 0; i < tmp.getLength(); i++) {
            tmp.set(i, offset + amplitude * (float) Math.sin((double) i / (double) period * 2 * Math.PI));
        }
        for (int i = 0; i < l1; i++) {
            float s = s1.get(o1 + i);
            if (add) {
                s += tmp.get(i % tmp.getLength());
            } else {
                s = tmp.get(i % tmp.getLength());
            }
            s1.set(i + o1, ch1.mixIntensity(i + o1, s1.get(i + o1), s));
        }
    }
