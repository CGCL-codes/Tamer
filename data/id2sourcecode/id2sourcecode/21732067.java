    public void sendControlChange(int channel, int controller, int value) {
        if (controller == MidiControllers.BANK_SELECT) {
            this.jackOutputPort.getRouter().setBank(channel, value);
        } else {
            byte[] event = new byte[3];
            event[0] = (byte) (0xB0 | this.jackOutputPort.getRouter().getChannelRoute(channel));
            event[1] = (byte) controller;
            event[2] = (byte) value;
            this.jackClient.addEventToQueue(this.jackOutputPort.getRouter().getPortRoute(channel), event);
        }
    }
