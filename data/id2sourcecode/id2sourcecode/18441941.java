    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        for (int i = 0; i < l1; i++) {
            if ((i + o1) % period != phase) {
                if (add) {
                    s1.set(i + o1, s1.get(i + o1) + ch1.mixIntensity(i + o1, s1.get(i + o1), 0));
                } else {
                    s1.set(i + o1, ch1.mixIntensity(i + o1, s1.get(i + o1), 0));
                }
            }
        }
    }
