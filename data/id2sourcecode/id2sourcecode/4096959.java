    public JavaSoundMidiKar() {
        try {
            sequencer = MidiSystem.getSequencer();
            if (sequencer instanceof Synthesizer) synthesizer = (Synthesizer) sequencer; else synthesizer = MidiSystem.getSynthesizer();
            channels = synthesizer.getChannels();
            if (!(sequencer instanceof Synthesizer)) {
                Transmitter seqTrans = sequencer.getTransmitter();
                Receiver synthRecv = synthesizer.getReceiver();
                seqTrans.setReceiver(synthRecv);
            }
            visualKaraoke = new Karaoke(sequencer);
            sequencer.addMetaEventListener(this);
        } catch (MidiUnavailableException ex) {
            System.out.println("Your system does not support midi sound !");
        }
    }
