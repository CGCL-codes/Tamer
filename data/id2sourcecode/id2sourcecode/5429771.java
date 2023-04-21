    private void handleMessageSender(MessageSender sender) {
        if (logger.isDebugEnabled()) logger.debug("Received Message " + sender.getMessage() + " from the DataSession.");
        GroupSendableEvent event = null;
        if (sender.getDestination() == null) {
            try {
                event = new JGCSGroupEvent(sender.getChannel(), Direction.DOWN, this, this.myGroup, this.vs.id);
                event.setMessage(sender.getMessage());
            } catch (AppiaEventException e) {
                e.printStackTrace();
            }
        } else {
            try {
                event = new JGCSSendEvent(sender.getChannel(), Direction.DOWN, this, this.myGroup, this.vs.id);
                event.setMessage(sender.getMessage());
                ((JGCSSendEvent) event).setDestination(vs.getRankByAddress((InetSocketAddress) sender.getDestination()));
            } catch (AppiaEventException e) {
                e.printStackTrace();
            }
        }
        if (isBlocked) {
            eventsPending.add(event);
            logger.warn("The group is blocked. Message " + sender.getMessage() + " added to pending events.");
            return;
        }
        try {
            event.go();
        } catch (AppiaEventException e) {
            e.printStackTrace();
        }
        if (logger.isDebugEnabled()) logger.debug("Message " + sender.getMessage() + " Forwarded to the Channel " + event.getChannel().getChannelID());
    }
