    public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage) {
            ShortMessage msg = (ShortMessage) message;
            int velocity = msg.getData2();
            if (msg.getCommand() == ShortMessage.NOTE_ON && velocity != 0) {
                int channel = msg.getChannel();
                lastMIDIChannel = channel;
                int note = msg.getData1();
                lastMIDINote = note;
                this.monome.redrawPanel();
            }
        }
    }
