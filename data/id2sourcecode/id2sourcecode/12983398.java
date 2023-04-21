    public Notes(Sequence sequence) throws InvalidMidiDataException, IOException {
        Track[] tracks = sequence.getTracks();
        for (Track track : tracks) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent midiEvent = track.get(i);
                if (midiEvent.getMessage() instanceof ShortMessage) {
                    ShortMessage shortMessage = (ShortMessage) midiEvent.getMessage();
                    if (shortMessage.getCommand() == ShortMessage.NOTE_ON && shortMessage.getData2() != 0) {
                        long onset = midiEvent.getTick();
                        int midiNoteNumber = shortMessage.getData1();
                        int channel = shortMessage.getChannel();
                        long duration = 0;
                        boolean found = false;
                        for (int j = i + 1; j < track.size() && !found; j++) {
                            MidiEvent midiEvent2 = track.get(j);
                            if (midiEvent2.getMessage() instanceof ShortMessage) {
                                ShortMessage shortMessage2 = (ShortMessage) midiEvent2.getMessage();
                                if (shortMessage2.getChannel() == channel && (shortMessage2.getData1() == midiNoteNumber) && (shortMessage2.getCommand() == ShortMessage.NOTE_OFF || (shortMessage2.getCommand() == ShortMessage.NOTE_ON && shortMessage2.getData2() == 0))) {
                                    duration = midiEvent2.getTick() - onset;
                                    found = true;
                                }
                            }
                        }
                        if (!found) System.out.println("WARNING! Failed to find duration of MIDI event: onset = " + onset + ", note number = " + midiNoteNumber + ", channel = " + channel);
                        addNote(new Note(onset, midiNoteNumber, duration, channel, this));
                    }
                }
            }
        }
    }
