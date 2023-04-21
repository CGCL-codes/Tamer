    public static void setVolume(int value) {
        try {
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            Receiver receiver = synthesizer.getReceiver();
            Sequencer sequencer = MidiSystem.getSequencer(false);
            Transmitter transmitter = sequencer.getTransmitter();
            transmitter.setReceiver(receiver);
            javax.sound.midi.MidiChannel[] channels = synthesizer.getChannels();
            ShortMessage volumeMessage = new ShortMessage();
            for (int i = 0; i < 9; i++) {
                volumeMessage.setMessage(ShortMessage.CONTROL_CHANGE, i, 7, value);
                MidiSystem.getReceiver().send(volumeMessage, -1);
            }
            for (int i = 11; i < channels.length; i++) {
                volumeMessage.setMessage(ShortMessage.CONTROL_CHANGE, i, 7, value);
                MidiSystem.getReceiver().send(volumeMessage, -1);
            }
        } catch (Exception e) {
        }
    }
