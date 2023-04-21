    public SmafEvent[] getSmafEvents(MidiEvent midiEvent, SmafContext context) throws InvalidSmafDataException {
        ShortMessage shortMessage = (ShortMessage) midiEvent.getMessage();
        int channel = shortMessage.getChannel();
        int data2 = shortMessage.getData2();
        int track = context.retrieveSmafTrack(channel);
        int voice = context.retrieveVoice(channel);
        ModulationMessage smafMessage = new ModulationMessage();
        smafMessage.setDuration(context.getDuration());
        smafMessage.setChannel(voice);
        smafMessage.setModulation(data2);
        context.setBeforeTick(track, midiEvent.getTick());
        return new SmafEvent[] { new SmafEvent(smafMessage, midiEvent.getTick()) };
    }
