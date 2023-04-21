    public final void appendMessage(final byte[] message) throws IOException {
        synchronized (file) {
            MessageAppender appender = new MessageAppender(getChannel());
            long newMessagePosition = appender.appendMessage(message);
            if (messagePositions != null) {
                Long[] newMessagePositions = new Long[messagePositions.length + 1];
                System.arraycopy(messagePositions, 0, newMessagePositions, 0, messagePositions.length);
                newMessagePositions[newMessagePositions.length - 1] = newMessagePosition;
                messagePositions = newMessagePositions;
            }
        }
        clearBufferCache();
    }
