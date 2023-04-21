    public String toString() {
        return "MainConnection( " + ((TCPSession) getChannel().getSession()).getSocket() + " )";
    }
