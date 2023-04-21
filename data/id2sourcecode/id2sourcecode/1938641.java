    public void transposeMeasure(UndoableJoined undoableJoined, TGMeasure measure, int transposition, boolean tryKeepString, boolean applyToChords) {
        if (transposition != 0 && !getSongManager().isPercussionChannel(measure.getTrack().getChannelId())) {
            UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo(measure);
            getSongManager().getMeasureManager().transposeNotes(measure, transposition, tryKeepString, applyToChords, -1);
            undoableJoined.addUndoableEdit(undoable.endUndo(measure));
        }
    }
