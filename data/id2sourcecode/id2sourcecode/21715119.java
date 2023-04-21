    public void update() {
        TGLayout layout = TuxGuitar.instance().getTablatureEditor().getTablature().getViewLayout();
        int style = layout.getStyle();
        this.showToolbars.setSelection(TuxGuitar.instance().getItemManager().isCoolbarVisible());
        this.showInstruments.setSelection(!TuxGuitar.instance().getChannelManager().isDisposed());
        this.showTransport.setSelection(!TuxGuitar.instance().getTransport().isDisposed());
        this.showFretBoard.setSelection(TuxGuitar.instance().getFretBoardEditor().isVisible());
        this.showPiano.setSelection(!TuxGuitar.instance().getPianoEditor().isDisposed());
        this.showMatrix.setSelection(!TuxGuitar.instance().getMatrixEditor().isDisposed());
        this.pageLayout.setSelection(layout instanceof TGLayoutVertical);
        this.linearLayout.setSelection(layout instanceof TGLayoutHorizontal);
        this.multitrack.setSelection((style & TGLayout.DISPLAY_MULTITRACK) != 0);
        this.scoreEnabled.setSelection((style & TGLayout.DISPLAY_SCORE) != 0);
        this.tablatureEnabled.setSelection((style & TGLayout.DISPLAY_TABLATURE) != 0);
        this.compact.setSelection((style & TGLayout.DISPLAY_COMPACT) != 0);
        this.compact.setEnabled((style & TGLayout.DISPLAY_MULTITRACK) == 0 || layout.getSongManager().getSong().countTracks() == 1);
        this.chordName.setSelection((style & TGLayout.DISPLAY_CHORD_NAME) != 0);
        this.chordDiagram.setSelection((style & TGLayout.DISPLAY_CHORD_DIAGRAM) != 0);
    }
