    private void controlChange(int nManual, int nController, int nValue) {
        ShortMessage sm = new ShortMessage();
        try {
            sm.setMessage(ShortMessage.CONTROL_CHANGE, getChannel(nManual), nController, nValue);
        } catch (InvalidMidiDataException e) {
            Debug.out(e);
        }
        getReceiver(nManual).send(sm, -1);
    }
