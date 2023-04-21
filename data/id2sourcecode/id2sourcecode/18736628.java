    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        ch1.getChannel().markChange();
        MMArray x = new MMArray(2, 0);
        MMArray y = new MMArray(2, 0);
        x.set(0, 0);
        x.set(1, l1);
        if (normalized) {
            y.set(0, o1);
            y.set(1, o1 + l1);
        } else {
            y.set(0, startAmplitude);
            y.set(1, endAmplitude);
        }
        if (add) {
            for (int i = 0; i < l1; i++) {
                s1.set(o1 + i, ch1.mixIntensity(i + o1, s1.get(o1 + i), s1.get(o1 + i) + AOToolkit.interpolate1(x, y, i)));
            }
        } else {
            for (int i = 0; i < l1; i++) {
                s1.set(o1 + i, ch1.mixIntensity(i + o1, s1.get(o1 + i), AOToolkit.interpolate1(x, y, i)));
            }
        }
    }
