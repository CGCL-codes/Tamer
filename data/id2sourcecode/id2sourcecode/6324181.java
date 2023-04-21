    public void exec(Command command) throws IOException {
        if (command.getChannel() == FlapConstants.FLAP_CHANNEL_SNAC) {
            if (command.getFamily() == SNACConstants.SNAC_FAMILY_GENERIC_SERVICE_CONTROLS) {
                if (command.getSubType() == SNACConstants.MSG_OF_THE_DAY) {
                    log.info("ignoring the MSG OF THE DAY");
                } else if (command.getSubType() == SNACConstants.SRV_SVC_VERSIONS) {
                    listeners.eventHappened(new IMEvent(this, OscarEventName.bosServiceList));
                }
            }
        }
    }
