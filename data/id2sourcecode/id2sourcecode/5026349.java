    public void send(MidiMessage message, long timeStamp) {
        ShortMessage msg = (ShortMessage) message;
        int x = msg.getData1() - 12;
        int y = msg.getChannel();
        if (x >= 0 && x < this.monome.sizeX && y >= 0 && y < this.monome.sizeY) {
            int velocity = msg.getData2();
            if (velocity == 127) {
                this.toggleValues[x][y] = 1;
            } else {
                this.toggleValues[x][y] = 0;
            }
            this.redrawMonome();
        }
        return;
    }
