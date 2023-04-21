    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        float p = startPeriod;
        double rad = 0;
        double exp = Math.pow((double) endPeriod / startPeriod, 1. / l1);
        ch1.getChannel().markChange();
        for (int i = 0; i < l1; i++) {
            p *= exp;
            rad += 1. / p * 2 * Math.PI;
            float s = 0;
            if (add) {
                s = s1.get(o1 + i);
            }
            s += offset + amplitude * (float) Math.sin(rad);
            s1.set(i + o1, ch1.mixIntensity(i + o1, s1.get(i + o1), s));
        }
    }
