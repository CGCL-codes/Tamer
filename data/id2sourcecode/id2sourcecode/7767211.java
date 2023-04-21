    protected void cancelConnection(DaapConnectionNIO connection) {
        SelectionKey sk = connection.getChannel().keyFor(selector);
        cancel(sk);
    }
