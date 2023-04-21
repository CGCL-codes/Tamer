    public int getLocalPort() {
        if (session.getChannel() != null) return session.getChannel().socket().getLocalPort();
        return -1;
    }
