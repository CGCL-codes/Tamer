    public final void leaveFromChannel(final ClientSession client) {
        if (client != null) {
            getChannel().leave(client);
        }
    }
