    public UndoableTrackInstrument endUndo(TGTrack track) {
        this.redoCaret = new UndoableCaretHelper();
        this.redoChannelId = track.getChannelId();
        return this;
    }
