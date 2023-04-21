    public void sendProgramChange(int channel, int value) {
        this.jackOutputPort.getRouter().setProgram(channel, value);
        byte[] event1 = new byte[3];
        event1[0] = (byte) (0xB0 | this.jackOutputPort.getRouter().getChannelRoute(channel));
        event1[1] = (byte) MidiControllers.BANK_SELECT;
        event1[2] = (byte) this.jackOutputPort.getRouter().getBankRoute(channel);
        byte[] event2 = new byte[2];
        event2[0] = (byte) (0xC0 | this.jackOutputPort.getRouter().getChannelRoute(channel));
        event2[1] = (byte) this.jackOutputPort.getRouter().getProgramRoute(channel, value);
        this.jackClient.addEventToQueue(this.jackOutputPort.getRouter().getPortRoute(channel), event1);
        this.jackClient.addEventToQueue(this.jackOutputPort.getRouter().getPortRoute(channel), event2);
    }
