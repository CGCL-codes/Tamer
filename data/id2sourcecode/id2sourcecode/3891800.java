    public void start() {
        id = (int) (Math.random() * Integer.MAX_VALUE);
        connect();
        Thread reader = new Thread(new IncomingReader());
        reader.start();
        Thread writer = new Thread(sender);
        writer.start();
    }
