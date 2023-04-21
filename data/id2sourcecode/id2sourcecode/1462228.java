    ConnectionDescriptor(SocketChannel upStream, SocketChannel downStream) throws IOException {
        this.upStream = upStream;
        this.downStream = downStream;
        connectionId = counter.getAndIncrement();
        logStream = new FileOutputStream("log-" + System.currentTimeMillis() + "-" + connectionId + ".txt").getChannel();
    }
