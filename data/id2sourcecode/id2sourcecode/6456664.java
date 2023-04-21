    public final boolean isActive() {
        return reconnectAttempt == 0 && getChannel() != null && getChannel().isConnected();
    }
