    public void updateChannel(boolean percussionChanged) {
        if (getChannel() != null && !isDisposed()) {
            boolean percussionChannel = this.percussionButton.getSelection();
            int bank = getChannel().getBank();
            int program = getChannel().getProgram();
            if (percussionChanged) {
                bank = (percussionChannel ? TGChannel.DEFAULT_PERCUSSION_BANK : TGChannel.DEFAULT_BANK);
                program = (percussionChannel ? TGChannel.DEFAULT_PERCUSSION_PROGRAM : TGChannel.DEFAULT_PROGRAM);
            } else {
                if (!percussionChannel) {
                    int bankSelection = this.bankCombo.getSelectionIndex();
                    if (bankSelection >= 0) {
                        bank = bankSelection;
                    }
                }
                int programSelection = this.programCombo.getSelectionIndex();
                if (programSelection >= 0) {
                    program = programSelection;
                }
            }
            getHandle().updateChannel(getChannel().getChannelId(), (short) bank, (short) program, (short) this.volumeScale.getValue(), (short) this.balanceScale.getValue(), (short) this.chorusScale.getValue(), (short) this.reverbScale.getValue(), (short) this.phaserScale.getValue(), (short) this.tremoloScale.getValue(), this.nameText.getText());
        }
    }
