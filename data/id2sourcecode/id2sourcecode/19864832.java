    public void stopNote(MIDINote note, int channel) {
        if (!JMIDI.isReady()) return;
        JMIDI.getChannel(channel).noteOff(note.getPitch());
    }
