    public void send(MidiMessage message, long lTimeStamp) {
        ShortMessage shortMessage;
        for (int i = 0; i < this.numMonomeConfigurations; i++) {
            this.monomeConfigurations.get(i).send(message, lTimeStamp);
        }
        if (message instanceof ShortMessage) {
            shortMessage = (ShortMessage) message;
            switch(shortMessage.getCommand()) {
                case 0xF0:
                    if (shortMessage.getChannel() == 8) {
                        for (int i = 0; i < this.numMonomeConfigurations; i++) {
                            this.monomeConfigurations.get(i).tick();
                        }
                    }
                    if (shortMessage.getChannel() == 0x0C) {
                        for (int i = 0; i < this.numMonomeConfigurations; i++) {
                            this.monomeConfigurations.get(i).reset();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }
