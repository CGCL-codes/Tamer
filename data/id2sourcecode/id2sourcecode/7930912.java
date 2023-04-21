    private void addNote(long noteStart_ppq, long bufferEnd_ppq) throws InvalidMidiDataException {
        if (randomGenerator.nextDouble() > density) return;
        int pitch = minimumPitch + randomGenerator.nextInt(maximumPitch - minimumPitch + 1);
        int length_ppq = minNoteLength_ppq + randomGenerator.nextInt(maxNoteLength_ppq - minNoteLength_ppq + 1);
        ShortMessage on = new ShortMessage();
        for (Track midiTrack : trackMap.values()) {
            on.setMessage(ShortMessage.NOTE_ON, midiTrack.getChannel(), pitch, NOTE_VELOCITY);
            midiTrack.addMidiMessage(noteStart_ppq, on);
        }
        ShortMessage off = new ShortMessage();
        if ((noteStart_ppq + length_ppq) >= bufferEnd_ppq) {
            off.setMessage(ShortMessage.NOTE_OFF, 0, pitch, NOTE_VELOCITY);
            midiEventArrayList.add(new MIDIEvent(off, noteStart_ppq + length_ppq));
        } else {
            for (Track midiTrack : trackMap.values()) {
                off.setMessage(ShortMessage.NOTE_OFF, midiTrack.getChannel(), pitch, NOTE_VELOCITY);
                midiTrack.addMidiMessage(noteStart_ppq + length_ppq, off);
            }
        }
    }
