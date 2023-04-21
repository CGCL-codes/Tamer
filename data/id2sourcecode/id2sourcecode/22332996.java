    public void update() {
        this.showFretBoard.setSelection(TuxGuitar.instance().getFretBoardEditor().isVisible());
        this.showInstruments.setSelection(!TuxGuitar.instance().getChannelManager().isDisposed());
        this.showTransport.setSelection(!TuxGuitar.instance().getTransport().isDisposed());
    }
