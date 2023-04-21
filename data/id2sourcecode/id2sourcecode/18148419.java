    public void run() {
        try {
            Socket acceptedConnection = socket.accept();
            SocketChannel channel = acceptedConnection.getChannel();
            channel.configureBlocking(true);
            doWork(channel);
            latch.await();
            acceptedConnection.close();
            socket.close();
        } catch (Exception e) {
            System.err.println("Exception occurred in server emulator");
            e.printStackTrace();
            errorOccurred = true;
        }
    }
