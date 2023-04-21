    protected boolean updateTracks(long bufferStart, long bufferLength) throws InvalidMidiDataException {
        for (TrackBufferUpdateListener tbul : trackBufferUpdateListenerArray) tbul.trackBufferUpdate();
        for (Track midiTrack : trackMap.values()) {
            TreeMap<Long, ArrayList<ShortMessage>> midiMessageArrays = midiTrack.getMidiMessages();
            for (ArrayList<ShortMessage> messageArray : midiMessageArrays.values()) {
                for (ShortMessage tempMessage : messageArray) {
                    if (tempMessage.getCommand() == ShortMessage.NOTE_ON || tempMessage.getCommand() == ShortMessage.NOTE_OFF) {
                        int newNote = tempMessage.getData1() + pitchShiftAmount;
                        if (newNote > 127) newNote = 127; else if (newNote < 0) newNote = 0;
                        tempMessage.setMessage(tempMessage.getCommand(), tempMessage.getChannel(), newNote, tempMessage.getData2());
                    }
                }
            }
        }
        return true;
    }
