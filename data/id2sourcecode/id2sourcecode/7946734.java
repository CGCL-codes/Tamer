    @Override
    public void send(IndirectResponseMessage message) {
        send(new RawResponseMessage("PRIVMSG " + getChannel() + " :\001ACTION " + message.getText() + "\001", message.getProbability(), message.getSleepTime()));
    }
