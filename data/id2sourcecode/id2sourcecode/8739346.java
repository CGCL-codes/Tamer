    public synchronized void close() {
        if (isClosed()) return;
        closed = true;
        if (!isKeepAlive()) {
            Channel channel = getChannel();
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (ConnectionListner listner : listners) {
            listner.connectionClosed(this);
        }
    }
