    public void setupChannel() {
        if (getChannel() != null && !isDisposed()) {
            TGChannelSettingsDialog settingsDialog = this.dialog.getChannelSettingsHandlerManager().findChannelSettingsDialog();
            if (settingsDialog != null) {
                settingsDialog.show(this.dialog.getShell(), getChannel());
            }
        }
    }
