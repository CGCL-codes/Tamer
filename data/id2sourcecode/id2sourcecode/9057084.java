    public static UndoableChannelGeneric startUndo() {
        UndoableChannelGeneric undoable = new UndoableChannelGeneric();
        undoable.doAction = UNDO_ACTION;
        undoable.undoCaret = new UndoableCaretHelper();
        undoable.undoChannels = undoable.getChannels();
        return undoable;
    }
