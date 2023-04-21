    public MidiEvent getNoteOffMidiEvent() throws NoSuchElementException {
        ShortMessage shortMessage = null;
        MidiEvent midiEvent = midiEvents.get(midiEventIndex);
        MidiMessage midiMessage = midiEvent.getMessage();
        if (midiMessage instanceof ShortMessage) {
            shortMessage = (ShortMessage) midiMessage;
        } else {
            throw new IllegalStateException("current is not ShortMessage");
        }
        int channel = shortMessage.getChannel();
        int data1 = shortMessage.getData1();
        for (int i = midiEventIndex + 1; i < midiEvents.size(); i++) {
            midiEvent = midiEvents.get(i);
            midiMessage = midiEvent.getMessage();
            if (midiMessage instanceof ShortMessage) {
                shortMessage = (ShortMessage) midiMessage;
                if (shortMessage.getChannel() == channel && shortMessage.getData1() != data1 && shortMessage.getData2() == 0 && !noteOffEventUsed.get(i)) {
                    noteOffEventUsed.set(i);
                    return midiEvent;
                }
            }
        }
        throw new NoSuchElementException(channel + "ch, " + data1);
    }
