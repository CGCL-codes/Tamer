    public Channel getInChannel() {
        if (hasInMessage()) {
            return getInMessage().getChannel();
        }
        return getDeadLetterChannel();
    }
