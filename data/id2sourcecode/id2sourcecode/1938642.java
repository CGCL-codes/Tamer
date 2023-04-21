    public void transposeTrack(UndoableJoined undoableJoined, TGTrack track, int transposition, boolean tryKeepString, boolean applyToChords) {
        if (transposition != 0 && !getSongManager().isPercussionChannel(track.getChannelId())) {
            UndoableTrackGeneric undoable = UndoableTrackGeneric.startUndo(track);
            getSongManager().getTrackManager().transposeNotes(track, transposition, tryKeepString, applyToChords, -1);
            undoableJoined.addUndoableEdit(undoable.endUndo(track));
        }
    }
