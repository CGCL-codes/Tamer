    @Override
    public boolean sendMessage(Message message) throws ConnectionException {
        if (!authenticated) throw new IllegalStateException("Must be authenticated to send a message.");
        if (message == null) throw new IllegalArgumentException("Null pointer in message");
        if (!(message instanceof AzetMessage)) throw new IllegalArgumentException("Illegal type of message: " + message.getClass());
        AzetMessage aMessage = (AzetMessage) message;
        if (aMessage.getReceiver() == null) throw new IllegalArgumentException("Null pointer in message receiver");
        if (aMessage.getReceiver().getName() == null) throw new IllegalArgumentException("Null pointer in message receiver name");
        if (aMessage.getContent() == null) throw new IllegalArgumentException("Null pointer in message content");
        if (aMessage.getContent().isEmpty()) throw new IllegalArgumentException("Message content is empty");
        if (!user.equals(aMessage.getSender())) throw new IllegalArgumentException("Message sender does not corespond to the authenticated user.");
        URL url;
        try {
            url = new URL(getProperty("messages.send.action"));
        } catch (NullPointerException ex) {
            throw new ConnectionException("No azet sendMessage url was found.", ex);
        } catch (MalformedURLException ex) {
            throw new ConnectionException("Azet sendMessage url is malformed", ex);
        }
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(getProperty("messages.send.receiver.id"), String.valueOf(aMessage.getReceiver().getId()));
        parameters.put(getProperty("messages.send.receiver.name"), aMessage.getReceiver().getName());
        if (aMessage.getSign() != null) {
            parameters.put(getProperty("messages.send.sign"), aMessage.getSign());
        }
        if (aMessage.getTimeOfPrevious() != null) {
            parameters.put(getProperty("messages.send.timeOfPrevious"), MessagesParser.ADVANCED_SEND_TIME_FORMAT.format(aMessage.getTimeOfPrevious()));
        }
        if (aMessage.getTextOfPrevious() != null) {
            parameters.put(getProperty("messages.send.textOfPrevious"), aMessage.getTextOfPrevious());
        }
        for (int i = 0; getProperty("messages.send.hidden" + i + ".name") != null; i++) {
            parameters.put(getProperty("messages.send.hidden" + i + ".name"), getProperty("messages.send.hidden" + i + ".value"));
        }
        List<String> contents = splitterCondenser.split(aMessage);
        aMessage.setLastFragment(contents.get(contents.size() - 1));
        for (String content : contents) {
            parameters.put(getProperty("messages.send.content"), content);
            try {
                HttpURLHandler urlHandler = new HttpURLHandler(url, getProperty("messages.send.method"), parameters, getProperty("encoding"));
                if (!messageSender.parse(urlHandler.getInputStream())) {
                    return false;
                }
            } catch (IOException ex) {
                throw new ConnectionException("Unable to read from/write to azet sendMessage url", ex);
            }
        }
        return true;
    }
