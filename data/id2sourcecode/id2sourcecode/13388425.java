    public void exec(Command command) throws IOException {
        if (command.getChannel() == FlapConstants.FLAP_CHANNEL_SNAC) {
            if ((command.getFamily() == SNACConstants.SNAC_FAMILY_GENERIC_SERVICE_CONTROLS) && (command.getSubType() == SNACConstants.FAMILY_LIST)) {
                listeners.eventHappened(new IMEvent(this, OscarEventName.bosConnected));
            }
        }
    }
