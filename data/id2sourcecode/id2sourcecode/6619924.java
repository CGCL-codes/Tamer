    protected void eventListener(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            sendSysex(1);
        } else {
            sendSysex(0);
        }
        try {
            Thread.sleep(100);
        } catch (Exception ex) {
        }
        ((Driver) thisPatch.getDriver()).send(sndChange(((Driver) thisPatch.getDriver()).getChannel()));
    }
