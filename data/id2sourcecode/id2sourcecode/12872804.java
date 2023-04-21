    public int getChannel() throws ProcessingException {
        int channel = -1;
        for (KeyMessage message : getMessages(KeyMessage.class)) {
            Equal equal = Command.get(message.get(Message.STATUS), Equal.class);
            if (equal != null) {
                channel = ((int) equal.getValue()) & 0x0f;
            }
        }
        if (channel == -1) {
            throw new ProcessingException();
        }
        return channel;
    }
