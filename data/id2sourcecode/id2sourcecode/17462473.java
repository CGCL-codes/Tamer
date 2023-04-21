    public void setChannelPressure(int nPressure) {
        ShortMessage message = new ShortMessage();
        try {
            message.setMessage(ShortMessage.CHANNEL_PRESSURE, getChannel(), nPressure, 0);
        } catch (InvalidMidiDataException e) {
            if (TDebug.TraceAlsaMidiChannel || TDebug.TraceAllExceptions) {
                TDebug.out(e);
            }
        }
        sendMessage(message);
    }
