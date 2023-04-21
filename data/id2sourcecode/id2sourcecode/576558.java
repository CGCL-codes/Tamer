    public static void main(String[] args) {
        int nChannelNumber = 0;
        int nNoteNumber = 0;
        int nVelocity = 0;
        int nDuration = 0;
        int nNoteNumberArgIndex = 0;
        switch(args.length) {
            case 4:
                nChannelNumber = Integer.parseInt(args[0]) - 1;
                nChannelNumber = Math.min(15, Math.max(0, nChannelNumber));
                nNoteNumberArgIndex = 1;
            case 3:
                nNoteNumber = Integer.parseInt(args[nNoteNumberArgIndex]);
                nNoteNumber = Math.min(127, Math.max(0, nNoteNumber));
                nVelocity = Integer.parseInt(args[nNoteNumberArgIndex + 1]);
                nVelocity = Math.min(127, Math.max(0, nVelocity));
                nDuration = Integer.parseInt(args[nNoteNumberArgIndex + 2]);
                nDuration = Math.max(0, nDuration);
                break;
            default:
                printUsageAndExit();
        }
        Synthesizer synth = null;
        try {
            synth = MidiSystem.getSynthesizer();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        }
        if (DEBUG) out("Synthesizer: " + synth);
        try {
            synth.open();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        }
        MidiChannel[] channels = synth.getChannels();
        MidiChannel channel = channels[nChannelNumber];
        if (DEBUG) out("MidiChannel: " + channel);
        channel.noteOn(nNoteNumber, nVelocity);
        try {
            Thread.sleep(nDuration);
        } catch (InterruptedException e) {
        }
        channel.noteOff(nNoteNumber);
        synth.close();
    }
