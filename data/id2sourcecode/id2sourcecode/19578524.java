    public void processMessage(MidiMessage message) {
        if (message instanceof ShortMessage) {
            ShortMessage sms = (ShortMessage) message;
            processMessage(sms.getChannel(), sms.getCommand(), sms.getData1(), sms.getData2());
            return;
        }
        processMessage(message.getMessage());
    }
