    public boolean isCellEditable(int trk, int col) {
        MidiTrack track = getSequence().getMidiTrack(trk);
        if (col == MUTE) return true;
        if (track.getChannel() >= 0) {
            if (col == SOLO) return true;
            if (col == PROGRAM) return true;
            if (col == BANK) return true;
            if (col == CHANNEL) return true;
        }
        if (col == TRACK && trk > 0) return true;
        return false;
    }
