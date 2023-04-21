    protected void narrowing(AChannelSelection ch1, AChannelSelection ch2, int index, float wet) {
        float f = 1.f - (wet / 2);
        float fc = 1.f - f;
        float s1 = ch1.getChannel().getSamples().get(index);
        float s2 = ch2.getChannel().getSamples().get(index);
        if (modifyCh1) {
            float m = s1 * f + s2 * fc;
            ch1.getChannel().getSamples().set(index, ch1.mixIntensity(index, s1, m));
        }
        if (modifyCh2) {
            float m = s2 * f + s1 * fc;
            ch2.getChannel().getSamples().set(index, ch2.mixIntensity(index, s2, m));
        }
    }
