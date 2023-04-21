    public void sendControlChange(int controller, int value) throws MidiPlayerException {
        if (controller == MidiControllers.BANK_SELECT && value == PERCUSSION_BANK) {
            this.router.configureRoutes(this.route, true);
        }
        this.receiver.sendControlChange(this.route.getChannel1(), controller, value);
        if (this.route.getChannel1() != this.route.getChannel2()) {
            this.receiver.sendControlChange(this.route.getChannel2(), controller, value);
        }
    }
