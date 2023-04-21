    public void send(MidiMessage message, long timeStamp) {
        if (this.recv == null) {
            return;
        }
        ShortMessage shortMessage;
        if (message instanceof ShortMessage) {
            shortMessage = (ShortMessage) message;
            switch(shortMessage.getCommand()) {
                case 0xF0:
                    if (shortMessage.getChannel() == 0x08) {
                        this.recv.send(message, timeStamp);
                    }
                    if (shortMessage.getChannel() == 0x0A) {
                        this.recv.send(message, timeStamp);
                    }
                    if (shortMessage.getChannel() == 0x0C) {
                        this.recv.send(message, timeStamp);
                    }
                    break;
                default:
                    this.recv.send(message, timeStamp);
                    break;
            }
        }
    }
