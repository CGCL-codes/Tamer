    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        for (int i = 0; i < l1; i++) {
            s1.set(o1 + i, 0);
        }
    }
