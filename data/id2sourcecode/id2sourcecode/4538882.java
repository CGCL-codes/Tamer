    public void checkForNameModified() {
        if (getChannel() != null && !isDisposed() && !this.nameText.getText().equals(getChannel().getName())) {
            updateChannel(false);
        }
    }
