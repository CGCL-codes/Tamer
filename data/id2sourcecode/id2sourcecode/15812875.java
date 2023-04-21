    public String getLocalAddress() {
        if (session.getChannel() != null) return session.getChannel().socket().getLocalAddress().getHostAddress();
        return null;
    }
