    private void init() {
        Synth.startEngine(0);
        leftChannel = new SineOscillator();
        rightChannel = new SineOscillator();
        out = new LineOut();
        pinkNoise = new PinkNoise();
        pinkPanLeft = new PanUnit();
        pinkPanRight = new PanUnit();
        mixer = new SynthMixer(20, 4);
        connectPinkNoisePan(pinkPanLeft, pinkNoise);
        connectPinkNoisePan(pinkPanRight, pinkNoise);
        connectMixer(leftChannel.output, IS_LEFT, 0);
        connectMixer(rightChannel.output, IS_RIGHT, 1);
        connectMixer(pinkPanLeft.output, IS_LEFT, 2);
        connectMixer(pinkPanRight.output, IS_RIGHT, 3);
        mixer.connectOutput(LINEOUT_LEFT.getChannel(), out.input, JSynUtil.LEFT_CHANNEL);
        mixer.connectOutput(LINEOUT_RIGHT.getChannel(), out.input, JSynUtil.RIGHT_CHANNEL);
    }
