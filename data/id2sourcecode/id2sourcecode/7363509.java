    public ColTrack(Track t, int n, MIDIView v) {
        track = t;
        trackNum = n;
        viewer = v;
        setTempo(120, 36, 8);
        MidiChannel[] channels = synth.getChannels();
        if (trackNum < 16) chan = channels[trackNum]; else chan = channels[15];
    }
