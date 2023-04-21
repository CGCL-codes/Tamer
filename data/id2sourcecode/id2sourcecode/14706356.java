    public void send(MidiMessage message, long timeStamp) {
        ShortMessage sm = (ShortMessage) message;
        try {
            midiEventsModel.add(new MIDIEvent(timeStamp, sm.getCommand(), sm.getData1(), sm.getData2(), sm.getChannel()));
        } catch (InvalidMidiDataException ex) {
            MsgHandler.error(ex.getMessage());
            ex.printStackTrace();
        }
    }
