    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        try {
            for (int i = o1; i < (o1 + l1); i++) {
                samples++;
                sum += s1.get(i);
                sumOfSquares += s1.get(i) * s1.get(i);
                if (s1.get(i) > max) {
                    max = s1.get(i);
                }
                if (s1.get(i) < min) {
                    min = s1.get(i);
                }
                if (s1.get(i) > clippedThreshold) {
                    clippedSamples++;
                }
            }
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
    }
