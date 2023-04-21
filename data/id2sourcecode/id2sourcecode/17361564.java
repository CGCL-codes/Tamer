    public MidiEvent getNoteOffMidiEvent() throws NoSuchElementException {
        ShortMessage shortMessage = null;
        MidiEvent midiEvent = midiTrack.get(midiEventIndex);
        MidiMessage midiMessage = midiEvent.getMessage();
        if (midiMessage instanceof ShortMessage) {
            shortMessage = (ShortMessage) midiMessage;
        } else {
            throw new IllegalStateException("current is not ShortMessage");
        }
        int channel = shortMessage.getChannel();
        int data1 = shortMessage.getData1();
        for (int i = midiEventIndex + 1; i < midiTrack.size(); i++) {
            midiEvent = midiTrack.get(i);
            midiMessage = midiEvent.getMessage();
            if (midiMessage instanceof ShortMessage) {
                shortMessage = (ShortMessage) midiMessage;
                if (shortMessage.getChannel() == channel && shortMessage.getData1() == data1) {
                    noteOffEventUsed.set(i);
                    return midiEvent;
                }
            }
        }
        throw new NoSuchElementException(channel + "ch, " + data1);
    }
