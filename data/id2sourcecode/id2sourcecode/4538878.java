    private void updateBankCombo(boolean playerRunning) {
        if (!isDisposed() && getChannel() != null) {
            if (this.bankCombo.getItemCount() == 0) {
                String bankPrefix = TuxGuitar.getProperty("instrument.bank");
                for (int i = 0; i < 128; i++) {
                    this.bankCombo.add((bankPrefix + " #" + i));
                }
            }
            if (getChannel().getBank() >= 0 && getChannel().getBank() < this.bankCombo.getItemCount()) {
                this.bankCombo.select(getChannel().getBank());
            }
            this.bankCombo.setEnabled(!getChannel().isPercussionChannel());
        }
    }
