    protected void widening(AChannelSelection ch1, AChannelSelection ch2, int index, float wet) {
        int d = 1000;
        float f = 1.f - wet;
        float fc = 1.f - f;
        MMArray s1 = ch1.getChannel().getSamples();
        MMArray s2 = ch2.getChannel().getSamples();
        try {
            if (modifyCh1) {
                float m = s1.get(index) * f + s1.get(index + d) * fc;
                s1.set(index, ch1.mixIntensity(index, s1.get(index), m));
            } else if (modifyCh2) {
                float m = s2.get(index) * f + s2.get(index + d) * fc;
                s2.set(index, ch2.mixIntensity(index, s2.get(index), m));
            }
        } catch (Exception e) {
        }
    }
