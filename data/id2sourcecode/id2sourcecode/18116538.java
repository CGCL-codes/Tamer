    @Override
    protected void handleChannelMessage(MessageEvent event) {
        log.info(event.getChannel().getName() + ":" + event.getNick() + ":" + event.getMessage());
        if ("now die".equalsIgnoreCase(event.getMessage())) {
            event.getChannel().say("Okay, fine, I'll die");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            System.exit(0);
        }
    }
