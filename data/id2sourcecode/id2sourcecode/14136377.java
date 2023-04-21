    private void handleMessageSender(MessageSender sender) {
        if (sender.getDir() == Direction.DOWN) {
            JGCSSendableEvent event = null;
            try {
                event = new JGCSSendableEvent(sender.getChannel(), Direction.DOWN, this, sender.getDestination());
                event.setMessage(sender.getMessage());
            } catch (AppiaEventException e1) {
                e1.printStackTrace();
            }
            if (!receivedRSE) {
                eventsPending.add(event);
                return;
            } else {
                try {
                    event.go();
                } catch (AppiaEventException e) {
                    e.printStackTrace();
                }
            }
        } else {
            logger.warn("MessageSender event arrived from a bottom layer. This kind of events should appear only from the application.");
        }
    }
