    @Override
    public void send(ResponseMessage message) {
        send(new RawResponseMessage("PRIVMSG " + getChannel() + " :" + message.getText(), message.getProbability(), message.getSleepTime()));
    }
