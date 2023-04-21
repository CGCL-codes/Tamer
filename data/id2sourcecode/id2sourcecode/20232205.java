    public SmafEvent[] getSmafEvents(MidiEvent midiEvent, SmafContext context) throws InvalidSmafDataException {
        ShortMessage shortMessage = (ShortMessage) midiEvent.getMessage();
        int channel = shortMessage.getChannel();
        int track = context.retrieveSmafTrack(channel);
        int voice = context.retrieveVoice(channel);
        ExpressionMessage smafMessage = new ExpressionMessage();
        smafMessage.setDuration(context.getDuration());
        smafMessage.setChannel(voice);
        smafMessage.setVolume(shortMessage.getData2());
        context.setBeforeTick(track, midiEvent.getTick());
        return new SmafEvent[] { new SmafEvent(smafMessage, midiEvent.getTick()) };
    }
