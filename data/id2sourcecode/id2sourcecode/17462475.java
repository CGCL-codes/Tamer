    public void controlChange(int nController, int nValue) {
        ShortMessage message = new ShortMessage();
        try {
            message.setMessage(ShortMessage.CONTROL_CHANGE, getChannel(), nController, nValue);
        } catch (InvalidMidiDataException e) {
            if (TDebug.TraceAlsaMidiChannel || TDebug.TraceAllExceptions) {
                TDebug.out(e);
            }
        }
        sendMessage(message);
    }
