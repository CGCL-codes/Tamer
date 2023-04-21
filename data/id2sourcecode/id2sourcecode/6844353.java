    @Override
    public boolean updateTracks(long bufferStart_ppq, long bufferEnd_ppq) throws InvalidMidiDataException {
        for (Track midiTrack : trackMap.values()) {
            TreeMap<Long, ArrayList<ShortMessage>> tmpMsgMap = midiTrack.getMidiMessages();
            for (Long key : tmpMsgMap.keySet()) {
                for (ShortMessage msg : tmpMsgMap.get(key)) {
                    if (msg.getCommand() == ShortMessage.NOTE_ON || msg.getCommand() == ShortMessage.NOTE_OFF) {
                        int oldPitch = msg.getData1(), newPitch;
                        if (oldPitch > inversionPitch) newPitch = inversionPitch - (oldPitch - inversionPitch); else newPitch = inversionPitch + (inversionPitch - oldPitch);
                        if (newPitch < 0) newPitch = 0;
                        if (newPitch > 127) newPitch = 127;
                        msg.setMessage(msg.getCommand(), msg.getChannel(), newPitch, msg.getData2());
                    }
                }
            }
        }
        return true;
    }
