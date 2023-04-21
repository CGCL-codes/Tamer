    protected void eventListener(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            sendSysex(cb.getSelectedIndex() + getValueMin());
            if (autoUpdate) {
                try {
                    Thread.sleep(100);
                } catch (Exception ex) {
                }
                ((Driver) thisPatch.getDriver()).send(sndChange(((Driver) thisPatch.getDriver()).getChannel()));
            } else {
                updater.itemStateChanged();
            }
        }
    }
