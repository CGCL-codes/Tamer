    public void sendEvent(final MidiEvent event) {
        if (event.getChannel() > 15 || event.getChannel() < 0) {
            throw new InvalidMidiDataException("You tried to send to midi channel" + event.getChannel() + ". With midi you only have the channels 0 - 15 available.");
        }
        outputReceiver.send(event, -1);
    }
