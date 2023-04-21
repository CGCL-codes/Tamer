    public boolean process(ManagerEvent event) {
        if (getState() == Call.ACTIVE_STATE) {
            if (event instanceof HangupEvent) {
                HangupEvent he = (HangupEvent) event;
                if (he.getChannel().equals(channel.getDescriptor().getId())) {
                    setState(Call.INVALID_STATE, "Call Ended");
                    logger.info("Call Ended");
                    return true;
                }
            }
        }
        return false;
    }
