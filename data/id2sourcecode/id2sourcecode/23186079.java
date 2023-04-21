    public void sendEvent(final MidiEvent i_event) {
        if (i_event.getChannel() > 15 || i_event.getChannel() < 0) {
            throw new InvalidMidiDataException("You tried to send to midi channel" + i_event.getChannel() + ". With midi you only have the channels 0 - 15 available.");
        }
        i_event.setChannel(midiChannel);
        midiOutDevice.sendEvent(i_event);
    }
