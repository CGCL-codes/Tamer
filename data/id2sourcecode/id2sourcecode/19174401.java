    public void exec(Command command) throws IOException {
        if (command.getChannel() == FlapConstants.FLAP_CHANNEL_CONNECT) {
            listeners.eventHappened(new IMEvent(this, OscarEventName.bosConnecting));
        }
    }
