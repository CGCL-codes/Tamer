    public void createChain() {
        SampleIn sin = new SampleIn(this, fileName);
        sin.setWholeFile(wholeFile);
        ReSample reSample = new ReSample(sin, this.baseFreq);
        Oscillator sineMod = new Oscillator(this, Oscillator.SINE_WAVE, sin.getSampleRate(), sin.getChannels(), Oscillator.FREQUENCY, (float) modRate);
        sineMod.setAmp((float) this.modAmount * this.cutoff);
        Oscillator subOsc = new Oscillator(this, Oscillator.SQUARE_WAVE, sin.getSampleRate(), sin.getChannels());
        subOsc.setFrqRatio(0.5f);
        subOsc.setAmp((float) subAmp);
        Add adder = new Add(new AudioObject[] { reSample, subOsc });
        Filter filt = new Filter(new AudioObject[] { adder, sineMod }, this.cutoff, Filter.LOW_PASS);
        ADSR env = new ADSR(filt, attack, decay, sustain, release);
        Volume vol = new Volume(env);
        StereoPan span = new StereoPan(vol);
        SampleOut sout = new SampleOut(span);
    }
