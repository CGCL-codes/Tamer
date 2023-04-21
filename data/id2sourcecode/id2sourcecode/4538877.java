    public void updateItems() {
        if (!isDisposed() && getChannel() != null) {
            boolean playerRunning = this.getHandle().isPlayerRunning();
            boolean anyPercussionChannel = this.getHandle().isAnyPercussionChannel();
            boolean anyTrackConnectedToChannel = this.getHandle().isAnyTrackConnectedToChannel(getChannel());
            this.nameText.setText(getChannel().getName());
            this.percussionButton.setSelection(getChannel().isPercussionChannel());
            this.percussionButton.setEnabled(!anyTrackConnectedToChannel && (!anyPercussionChannel || getChannel().isPercussionChannel()));
            this.removeChannelButton.setEnabled(!anyTrackConnectedToChannel);
            this.setupChannelButton.setEnabled(this.dialog.getChannelSettingsHandlerManager().isChannelSettingsHandlerAvailable());
            this.volumeScale.setValue(getChannel().getVolume());
            this.balanceScale.setValue(getChannel().getBalance());
            this.reverbScale.setValue(getChannel().getReverb());
            this.chorusScale.setValue(getChannel().getChorus());
            this.tremoloScale.setValue(getChannel().getTremolo());
            this.phaserScale.setValue(getChannel().getPhaser());
            this.updateBankCombo(playerRunning);
            this.updateProgramCombo(playerRunning);
        }
    }
