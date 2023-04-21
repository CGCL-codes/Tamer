    public static UndoableTrackInstrument startUndo(TGTrack track) {
        UndoableTrackInstrument undoable = new UndoableTrackInstrument();
        undoable.doAction = UNDO_ACTION;
        undoable.undoCaret = new UndoableCaretHelper();
        undoable.trackNumber = track.getNumber();
        undoable.undoChannelId = track.getChannelId();
        return undoable;
    }
