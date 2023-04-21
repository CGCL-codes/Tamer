    public void receiveANS(Message message) {
        message.getChannel().getSession().terminate(ERR_UNEXPECTED_MESSAGE);
    }
