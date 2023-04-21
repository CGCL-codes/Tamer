    private void addNote(long notePosition_ppq, int notePitch, int noteLength_ppq, Track midiTrack) throws InvalidMidiDataException {
        try {
            ShortMessage on = new ShortMessage();
            on.setMessage(ShortMessage.NOTE_ON, midiTrack.getChannel(), notePitch, noteVelocity);
            midiTrack.addMidiMessage(notePosition_ppq, on);
            ShortMessage off = new ShortMessage();
            off.setMessage(ShortMessage.NOTE_OFF, midiTrack.getChannel(), notePitch, noteVelocity);
            if ((notePosition_ppq + noteLength_ppq) >= bufferEnd_ppq) {
                midiEventArrayList.add(new MIDIEvent(off, notePosition_ppq + noteLength_ppq));
            } else {
                midiTrack.addMidiMessage(notePosition_ppq + noteLength_ppq, off);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
