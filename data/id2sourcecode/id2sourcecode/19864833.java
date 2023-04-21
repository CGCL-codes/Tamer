    public void playNote(MIDINote note, int channel) {
        if (!JMIDI.isReady()) return;
        if (note.getPitch() != MIDINote.REST) {
            JMIDI.getChannel(channel).noteOn(note.getPitch(), note.getVolume());
        }
        CurrentNotes[channel] = note;
    }
