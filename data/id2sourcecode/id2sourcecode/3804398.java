    public void sendMessage(Message toSend) {
        io.addOutput("PRIVMSG " + toSend.getChannel() + " :" + toSend.getMessage() + "\r\n");
        io.sendOutputBuffer();
    }
