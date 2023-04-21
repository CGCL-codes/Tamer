    public void send(MidiMessage message, long timeStamp) {
        if (message instanceof ShortMessage) {
            ShortMessage sm = (ShortMessage) message;
            if (sm.getChannel() == channel) {
                if (sm.getCommand() == ShortMessage.NOTE_ON || sm.getCommand() == ShortMessage.NOTE_OFF) {
                    noteDown[sm.getData1()] = sm.getCommand() == ShortMessage.NOTE_ON && sm.getData1() > 0;
                    repaint();
                }
            }
        }
    }
