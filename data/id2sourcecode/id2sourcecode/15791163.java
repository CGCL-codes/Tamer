    public MfiEvent[] getMfiEvents(MidiEvent midiEvent, MfiContext context) throws InvalidMfiDataException {
        ShortMessage shortMessage = (ShortMessage) midiEvent.getMessage();
        int channel = shortMessage.getChannel();
        int data2 = shortMessage.getData2();
        int track = context.retrieveMfiTrack(channel);
        int voice = context.retrieveVoice(channel);
        VolumeMessage mfiMessage = new VolumeMessage();
        mfiMessage.setDelta(context.getDelta(context.retrieveMfiTrack(channel)));
        mfiMessage.setVoice(voice);
        mfiMessage.setVolume(data2 / 2);
        context.setPreviousTick(track, midiEvent.getTick());
        return new MfiEvent[] { new MfiEvent(mfiMessage, midiEvent.getTick()) };
    }
