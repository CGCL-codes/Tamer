    public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException, IOException {
        int nNoteNumber = 66;
        int nVelocity = 100;
        int nDuration = 2000;
        Soundbank soundbank = null;
        if (args.length == 1) {
            File file = new File(args[0]);
            soundbank = MidiSystem.getSoundbank(file);
            if (DEBUG) out("Soundbank: " + soundbank);
        } else if (args.length > 1) {
            printUsageAndExit();
        }
        Synthesizer synth = null;
        synth = MidiSystem.getSynthesizer();
        if (DEBUG) out("Synthesizer: " + synth);
        synth.open();
        if (DEBUG) out("Defaut soundbank: " + synth.getDefaultSoundbank());
        if (soundbank != null) {
            out("soundbank supported: " + synth.isSoundbankSupported(soundbank));
            boolean bInstrumentsLoaded = synth.loadAllInstruments(soundbank);
            if (DEBUG) out("Instruments loaded: " + bInstrumentsLoaded);
        }
        MidiChannel[] channels = synth.getChannels();
        channels[0].noteOn(nNoteNumber, nVelocity);
        try {
            Thread.sleep(nDuration);
        } catch (InterruptedException e) {
        }
        channels[0].noteOff(nNoteNumber);
    }
