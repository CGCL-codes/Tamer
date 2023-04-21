    public int getDuration() {
        int delta = 0;
        MidiEvent midiEvent = midiTrack.get(midiEventIndex);
        MidiMessage midiMessage = midiEvent.getMessage();
        if (midiMessage instanceof ShortMessage) {
            ShortMessage shortMessage = (ShortMessage) midiMessage;
            int channel = shortMessage.getChannel();
            delta = retrieveAdjustedDelta(retrieveSmafTrack(channel), midiEvent.getTick());
        } else if (midiMessage instanceof MetaMessage && ((MetaMessage) midiMessage).getType() == 81) {
            delta = retrieveAdjustedDelta(smafTrackNumber, midiEvent.getTick());
            Debug.println("delta for tempo[" + smafTrackNumber + "]: " + delta);
        } else {
            Debug.println("no delta defined for: " + midiMessage);
        }
        if (delta > 255) {
            Debug.println(Level.WARNING, "��: " + delta + ", " + (delta % 256));
        }
        return delta % 256;
    }
