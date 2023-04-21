    public void ping(Ping ping) {
        getChannel((byte) 2).write(ping);
    }
