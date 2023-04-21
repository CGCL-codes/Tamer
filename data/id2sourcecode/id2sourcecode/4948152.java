    public SmafEvent[] getSmafEvents(MidiEvent midiEvent, SmafContext context) throws InvalidSmafDataException {
        ShortMessage shortMessage = (ShortMessage) midiEvent.getMessage();
        int channel = shortMessage.getChannel();
        int data1 = shortMessage.getData1();
        int track = context.retrieveSmafTrack(channel);
        BankSelectMessage changeBankMessage = new BankSelectMessage();
        changeBankMessage.setDuration(context.getDuration());
        changeBankMessage.setChannel(channel % 4);
        changeBankMessage.setBank(data1);
        context.setBeforeTick(track, midiEvent.getTick());
        return new SmafEvent[] { new SmafEvent(changeBankMessage, midiEvent.getTick()) };
    }
