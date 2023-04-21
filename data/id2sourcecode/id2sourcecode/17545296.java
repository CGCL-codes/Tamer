    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        for (int i = o1; i < o1 + l1; i++) {
            float d = 0;
            switch(noiseType) {
                case WHITE:
                    d = (float) (offset + (2 * Math.random() - 1) * amplitude);
                    break;
                case TRIANGLE:
                    d = (float) (offset + (Math.random() + Math.random() - 1) * amplitude);
                    break;
                case GAUSSIAN:
                    d = (float) (offset + Math.sqrt(-2 * Math.log(Math.random())) * Math.cos(2 * Math.PI * Math.random()) * amplitude);
                    break;
            }
            if (add) {
                s1.set(i, ch1.mixIntensity(i, s1.get(i), s1.get(i) + d));
            } else {
                s1.set(i, ch1.mixIntensity(i, s1.get(i), d));
            }
        }
    }
