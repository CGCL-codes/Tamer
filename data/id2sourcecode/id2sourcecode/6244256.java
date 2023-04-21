    private TempNote getTempNote(int track, int channel, int value, boolean purge) {
        for (int i = 0; i < this.tempNotes.size(); i++) {
            TempNote note = (TempNote) this.tempNotes.get(i);
            if (note.getTrack() == track && note.getChannel() == channel && note.getValue() == value) {
                if (purge) {
                    this.tempNotes.remove(i);
                }
                return note;
            }
        }
        return null;
    }
