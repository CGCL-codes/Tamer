    public void send(MidiMessage message, long timeStamp) {
        if (!gui.syncCB.isSelected()) {
            return;
        }
        if (message instanceof ShortMessage) {
            ShortMessage shortMessage = (ShortMessage) message;
            switch(shortMessage.getCommand()) {
                case 0xF0:
                    if (shortMessage.getChannel() == 0x08 || shortMessage.getChannel() == 0x0A || shortMessage.getChannel() == 0x0C) {
                        monome.sendMidi(shortMessage, index);
                    }
                    break;
                default:
                    monome.sendMidi(shortMessage, index);
                    break;
            }
        }
    }
