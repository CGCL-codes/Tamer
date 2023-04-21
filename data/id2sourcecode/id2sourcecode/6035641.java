    public void sendProgramChange(int value) throws MidiPlayerException {
        this.receiver.sendProgramChange(this.route.getChannel1(), value);
        if (this.route.getChannel1() != this.route.getChannel2()) {
            this.receiver.sendProgramChange(this.route.getChannel2(), value);
        }
    }
