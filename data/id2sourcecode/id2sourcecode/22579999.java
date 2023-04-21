    public void addMessageConsumer(MessageConsumer messageConsumer) throws FilterException {
        this.messageConsumers.add(messageConsumer);
        log.debug(getLogHead() + "message consumer added for channel " + messageConsumer.getChannel());
        if (this.state.equals(LifecycleEnum.STARTED)) {
            try {
                messageConsumer.start();
                log.debug(getLogHead() + "message consumer for channel " + messageConsumer.getChannel() + " started.");
            } catch (LifecycleException e) {
                throw new FilterException("could not start message consumer for channel " + messageConsumer.getChannel() + ": " + e.getMessage(), e);
            }
        }
    }
