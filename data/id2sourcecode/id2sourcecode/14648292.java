    public boolean isPoisoned() throws IllegalStateException {
        if (isClosed()) {
            throw new IllegalStateException("Port is closed.");
        }
        return getChannel().isPoisoned();
    }
