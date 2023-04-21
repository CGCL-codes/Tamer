    public Channel getFaultChannel() {
        if (hasFaultMessage()) {
            return getInMessage().getChannel();
        }
        return getDeadLetterChannel();
    }
