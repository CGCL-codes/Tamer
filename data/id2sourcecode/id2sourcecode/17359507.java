    public void operate(AChannelSelection ch1) {
        MMArray s1 = ch1.getChannel().getSamples();
        int o1 = ch1.getOffset();
        int l1 = ch1.getLength();
        float pitchIndex = 0;
        ch1.getChannel().markChange();
        try {
            for (int i = 0; i < l1; i++) {
                float s = AOToolkit.interpolate3(baseSignal, pitchIndex);
                s1.set(i + o1, ch1.mixIntensity(i + o1, s1.get(i + o1), s));
                pitchIndex += constPitch;
            }
        } catch (ArrayIndexOutOfBoundsException oob) {
        }
    }
