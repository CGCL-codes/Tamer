    public void noteOn(int nNoteNumber, int nVelocity) {
        ShortMessage message = new ShortMessage();
        try {
            message.setMessage(ShortMessage.NOTE_ON, getChannel(), nNoteNumber, nVelocity);
        } catch (InvalidMidiDataException e) {
            if (TDebug.TraceAlsaMidiChannel || TDebug.TraceAllExceptions) {
                TDebug.out(e);
            }
        }
        sendMessage(message);
    }
