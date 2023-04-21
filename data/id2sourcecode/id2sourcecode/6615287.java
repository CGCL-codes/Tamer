    public final long appendMessage(final CharSequence message) throws IOException {
        return appendMessage(message, getChannel());
    }
