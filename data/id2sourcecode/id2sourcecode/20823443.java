    public SimpleSynth(int note) {
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            rcvr = synth.getReceiver();
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        }
        channels = synth.getChannels();
        channels[0].programChange(81);
        MidiMessage noteOn = getNoteOnMessage(note);
        rcvr.send(noteOn, 0);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MidiMessage noteOff = getNoteOffMessage(note);
        rcvr.send(noteOff, 0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        synth.close();
    }
