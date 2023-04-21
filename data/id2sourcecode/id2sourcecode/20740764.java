    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        for (int i = o1; i < o1 + l1; i++) {
            if (add) {
                s1.set(i, ch1.mixIntensity(i, s1.get(i), s1.get(i) + offset));
            } else {
                s1.set(i, ch1.mixIntensity(i, s1.get(i), offset));
            }
        }
    }
