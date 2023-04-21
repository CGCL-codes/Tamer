    public void sendMessage(String theName) throws MessagingException {
        message.setText(generateTheRealMessage(theName));
        Transport.send(message);
    }
