    public void sendPitchBend(int channel, int value) {
        byte[] event = new byte[3];
        event[0] = (byte) (0xE0 | this.jackOutputPort.getRouter().getChannelRoute(channel));
        event[1] = (byte) 0;
        event[2] = (byte) value;
        this.jackClient.addEventToQueue(this.jackOutputPort.getRouter().getPortRoute(channel), event);
    }
