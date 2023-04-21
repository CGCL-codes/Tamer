    public void send(MidiMessage message, long timeStamp) {
        ShortMessage msg = (ShortMessage) message;
        int x = msg.getData1() - 12;
        int y = msg.getChannel();
        if (x >= 0 && x < this.monome.sizeX && y >= 0 && y < this.monome.sizeY) {
            if (msg.getCommand() == ShortMessage.NOTE_ON) {
                this.toggleValues[x][y] = 1;
                this.monome.led(x, y, 1, this.index);
            } else {
                this.toggleValues[x][y] = 0;
                this.monome.led(x, y, 0, this.index);
            }
        }
        return;
    }
