    public void send(Message msg) throws MessagingException {
        if (msg != null) {
            Transport.send(msg);
        }
    }
