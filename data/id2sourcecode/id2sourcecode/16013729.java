    public Sample() {
        this.file = new File("foo.bar");
        setName("foo.bar");
        setColor(ColorScheme.DEFAULT_SAMPLE_COLOR);
        fsb = new FloatSampleBuffer(1, 1000, 44100);
        for (int i = 0; i < 1000; i++) {
            float value = i % 100 == 0 ? 1f : 0.1f;
            fsb.getChannel(0)[i] = value;
        }
        addStartAndEndSnapPoints();
        addSnapPointAt(100);
        addSnapPointAt(500);
        addSnapPointAt(900);
        for (int channel = 0; channel < fsb.getChannelCount(); channel++) {
            outputMap.addMapping(channel, channel);
        }
    }
