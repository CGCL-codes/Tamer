    public final boolean write(final Message message) {
        final boolean result = clientMessages.get(message.getClientId()).offer(message);
        totalMessageCount++;
        if (writerThread != null) {
            writerThread.interrupt();
        }
        return result;
    }
