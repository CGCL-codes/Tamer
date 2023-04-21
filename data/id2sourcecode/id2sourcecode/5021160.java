    public static final void playNote(int theChannel, int theKey, int theVelocity) {
        createSynth();
        _mySynthesizer.getChannels()[theChannel].noteOn(theKey, theVelocity);
    }
