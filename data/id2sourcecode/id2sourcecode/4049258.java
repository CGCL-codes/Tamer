    public boolean process(ManagerEvent event) {
        switch(state) {
            case Call.IDLE_STATE:
                if (event instanceof DialEvent) {
                    DialEvent dialEvent = (DialEvent) event;
                    if (dialEvent.getSrc().equals(callerChannel.getDescriptor().getId())) {
                        calledChannel.getDescriptor().setId(dialEvent.getDestination());
                        setState(Call.CONNECTING_STATE, "Dialing");
                        logger.info("DialEvent CONNECTING " + dialEvent.getDestination());
                        return true;
                    }
                } else if (event instanceof HangupEvent) {
                    HangupEvent hangupEvent = (HangupEvent) event;
                    if (hangupEvent.getChannel().equals(callerChannel.getDescriptor().getId())) {
                        setState(Call.INVALID_STATE, "No Route");
                        logger.info("No Route for caller " + callerChannel.getDescriptor().getId());
                        return true;
                    }
                }
                break;
            case Call.CONNECTING_STATE:
                if (event instanceof LinkEvent) {
                    LinkEvent linkEvent = (LinkEvent) event;
                    if (linkEvent.getChannel1().equals(callerChannel.getDescriptor().getId()) || linkEvent.getChannel2().equals(calledChannel.getDescriptor().getId())) {
                        setState(Call.ACTIVE_STATE, "Answered");
                        logger.info("LinkEvent Answered ACTIVE " + linkEvent.getChannel2());
                        return true;
                    }
                } else if (event instanceof HangupEvent) {
                    HangupEvent he = (HangupEvent) event;
                    if (he.getChannel().equals(callerChannel.getDescriptor().getId()) || he.getChannel().equals(calledChannel.getDescriptor().getId())) {
                        if (he.getCause().intValue() == 16) {
                            setState(Call.INVALID_STATE, "Canceled");
                            logger.info("Canceled");
                            return true;
                        } else if (he.getCause().intValue() == 21) {
                            setState(Call.INVALID_STATE, "Rejected");
                            logger.info("Rejected");
                            return true;
                        }
                    }
                } else if (event instanceof LinkEvent) {
                    LinkEvent le = (LinkEvent) event;
                    if (le.getChannel1().equals(callerChannel.getDescriptor().getId())) {
                        setState(Call.ACTIVE_STATE, "Answered");
                        logger.info("Answered");
                        return true;
                    }
                }
                break;
            case Call.ACTIVE_STATE:
                if (event instanceof UnlinkEvent) {
                    UnlinkEvent ue = (UnlinkEvent) event;
                    if (ue.getChannel1().equals(callerChannel.getDescriptor().getId())) {
                        setState(Call.INVALID_STATE, "Call Ended");
                        logger.info("Call Ended");
                        return true;
                    }
                } else if (event instanceof HangupEvent) {
                    HangupEvent he = (HangupEvent) event;
                    if (he.getChannel().equals(callerChannel.getDescriptor().getId()) || he.getChannel().equals(calledChannel.getDescriptor().getId())) {
                        if (he.getCause().intValue() == 16) {
                            setState(Call.INVALID_STATE, "Canceled");
                            logger.info("Canceled");
                            return true;
                        } else if (he.getCause().intValue() == 21) {
                            setState(Call.INVALID_STATE, "Rejected");
                            logger.info("Rejected");
                            return true;
                        }
                    }
                }
                break;
        }
        return false;
    }
