    public MfiEvent[] getMfiEvents(MidiEvent midiEvent, MfiContext context) throws InvalidMfiDataException {
        ShortMessage shortMessage = (ShortMessage) midiEvent.getMessage();
        int channel = shortMessage.getChannel();
        int track = context.retrieveMfiTrack(channel);
        int voice = context.retrieveVoice(channel);
        ExpressionMessage mfiMessage = new ExpressionMessage();
        mfiMessage.setDelta(context.getDelta(context.retrieveMfiTrack(channel)));
        mfiMessage.setVoice(voice);
        mfiMessage.setVolume(shortMessage.getData2() / 2);
        context.setPreviousTick(track, midiEvent.getTick());
        return new MfiEvent[] { new MfiEvent(mfiMessage, midiEvent.getTick()) };
    }
