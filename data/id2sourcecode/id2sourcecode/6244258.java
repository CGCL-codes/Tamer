    private void makeTempNotesBefore(long tick, int track) {
        long nextTick = tick;
        boolean check = true;
        while (check) {
            check = false;
            for (int i = 0; i < this.tempNotes.size(); i++) {
                TempNote note = (TempNote) this.tempNotes.get(i);
                if (note.getTick() < nextTick && note.getTrack() == track) {
                    nextTick = note.getTick() + (TGDuration.QUARTER_TIME * 5);
                    makeNote(nextTick, track, note.getChannel(), note.getValue());
                    check = true;
                    break;
                }
            }
        }
    }
