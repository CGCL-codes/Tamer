    public MfiEvent[] getMfiEvents(MidiEvent midiEvent, MfiContext context) throws InvalidMfiDataException {
        ShortMessage shortMessage = (ShortMessage) midiEvent.getMessage();
        int channel = shortMessage.getChannel();
        int data1 = shortMessage.getData1();
        int track = context.retrieveMfiTrack(channel);
        int voice = context.retrieveVoice(channel);
        ChangeVoiceMessage changeVoiceMessage = new ChangeVoiceMessage();
        changeVoiceMessage.setVoice(voice);
        changeVoiceMessage.setProgram(channel == 9 ? 0 : data1);
        ChangeBankMessage changeBankMessage = new ChangeBankMessage();
        changeBankMessage.setDelta(context.getDelta(context.retrieveMfiTrack(channel)));
        changeBankMessage.setVoice(channel % 4);
        changeBankMessage.setBank(((data1 & 0xc0) >> 6) + 2);
        context.setPreviousTick(track, midiEvent.getTick());
        return new MfiEvent[] { new MfiEvent(changeBankMessage, midiEvent.getTick()), new MfiEvent(changeVoiceMessage, midiEvent.getTick()) };
    }
