    public UndoableModifyChannel endUndo() {
        this.redoCaret = new UndoableCaretHelper();
        this.redoChannel = cloneChannel(getChannel());
        return this;
    }
