    @Override
    protected Transport getTransport(Session session) throws NoSuchProviderException {
        return new Transport(session, null) {

            @Override
            public void connect(String host, int port, String username, String password) throws MessagingException {
            }

            @Override
            public void close() throws MessagingException {
            }

            @Override
            public void sendMessage(Message message, Address[] recipients) throws MessagingException {
                Transport.send(message, recipients);
            }
        };
    }
