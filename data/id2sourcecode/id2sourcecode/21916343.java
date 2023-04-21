    private void addNoteOffMessages(long bufferStart_ppq) throws InvalidMidiDataException {
        for (Track midiTrack : trackMap.values()) {
            HashMap<Integer, Boolean> tempHashMap = noteOffHashMap.get(midiTrack.getID());
            if (tempHashMap != null) {
                for (Integer tempInteger : tempHashMap.keySet()) {
                    ShortMessage off = new ShortMessage();
                    off.setMessage(ShortMessage.NOTE_OFF, midiTrack.getChannel(), tempInteger.intValue(), 0);
                    midiTrack.addMidiMessage(bufferStart_ppq, off);
                }
            }
        }
        for (String trackKey : noteOffHashMap.keySet()) {
            noteOffHashMap.get(trackKey).clear();
        }
    }
