    public void send(MidiMessage message, long timeStamp) {
        ShortMessage shortMessage;
        if (message instanceof ShortMessage) {
            shortMessage = (ShortMessage) message;
            switch(shortMessage.getCommand()) {
                case 0x90:
                    if (shortMessage.getChannel() == 8) {
                        LEDBlink ledBlink = new LEDBlink(this.monome, 0, 6, this.delayBlinkingAmount, this.index);
                        new Thread(ledBlink).start();
                    }
                    if (shortMessage.getChannel() == 9) {
                        LEDBlink ledBlink = new LEDBlink(this.monome, 1, 6, this.delayBlinkingAmount, this.index);
                        new Thread(ledBlink).start();
                    }
                    if (shortMessage.getChannel() == 10) {
                        LEDBlink ledBlink = new LEDBlink(this.monome, 2, 6, this.delayBlinkingAmount, this.index);
                        new Thread(ledBlink).start();
                    }
                    if (shortMessage.getChannel() == 11) {
                        LEDBlink ledBlink = new LEDBlink(this.monome, 3, 6, this.delayBlinkingAmount, this.index);
                        new Thread(ledBlink).start();
                    }
                    if (shortMessage.getChannel() == 12) {
                        LEDBlink ledBlink = new LEDBlink(this.monome, 4, 6, this.delayBlinkingAmount, this.index);
                        new Thread(ledBlink).start();
                    }
                    if (shortMessage.getChannel() == 13) {
                        LEDBlink ledBlink = new LEDBlink(this.monome, 5, 6, this.delayBlinkingAmount, this.index);
                        new Thread(ledBlink).start();
                    }
                    if (shortMessage.getChannel() == 14) {
                        LEDBlink ledBlink = new LEDBlink(this.monome, 6, 6, this.delayBlinkingAmount, this.index);
                        new Thread(ledBlink).start();
                    }
                    if (shortMessage.getChannel() == 15) {
                        LEDBlink ledBlink = new LEDBlink(this.monome, 7, 6, this.delayBlinkingAmount, this.index);
                        new Thread(ledBlink).start();
                    }
                    break;
                default:
                    break;
            }
        }
    }
