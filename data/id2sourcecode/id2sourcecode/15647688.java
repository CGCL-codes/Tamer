    private void addNote(long noteStart_ppq, long bufferEnd_ppq, Track midiTrack) throws InvalidMidiDataException {
        ShortMessage on = new ShortMessage();
        on.setMessage(ShortMessage.NOTE_ON, midiTrack.getChannel(), notePitch, noteVelocity);
        midiTrack.addMidiMessage(noteStart_ppq, on);
        lastNote_ppq = noteStart_ppq;
        ShortMessage off = new ShortMessage();
        off.setMessage(ShortMessage.NOTE_OFF, midiTrack.getChannel(), notePitch, noteVelocity);
        if ((noteStart_ppq + noteLength_ppq) >= bufferEnd_ppq) {
            midiEventArrayList.add(new MIDIEvent(off, noteStart_ppq + noteLength_ppq));
        } else {
            midiTrack.addMidiMessage(noteStart_ppq + noteLength_ppq, off);
        }
    }
