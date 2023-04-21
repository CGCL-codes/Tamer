    public void noteOff(int nNoteNumber) {
        ShortMessage message = new ShortMessage();
        try {
            message.setMessage(ShortMessage.NOTE_OFF, getChannel(), nNoteNumber, 0);
        } catch (InvalidMidiDataException e) {
            if (TDebug.TraceAlsaMidiChannel || TDebug.TraceAllExceptions) {
                TDebug.out(e);
            }
        }
        sendMessage(message);
    }
