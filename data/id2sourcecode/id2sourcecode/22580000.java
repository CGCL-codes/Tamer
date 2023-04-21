    public void removeMessageConsumer(MessageConsumer messageConsumer) throws FilterException {
        boolean found = this.messageConsumers.remove(messageConsumer);
        if (found) {
            messageConsumer.stop();
        }
        log.debug(getLogHead() + "message consumer removed for channel " + messageConsumer.getChannel());
    }
