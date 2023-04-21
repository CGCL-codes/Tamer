    public void run() {
        while (run) {
            try {
                DataInputStream dis = new DataInputStream(session.getChannel().socket().getInputStream());
                int read = dis.readInt();
                log.info("Read magic number: " + read + ". Notification received. Passing notification to receiver...");
                m_Receiver.receive(session);
                errorCount = 0;
            } catch (Exception e) {
                errorCount++;
                log.warning("Error reading from socket. Error message is: " + e.getMessage());
                e.printStackTrace();
                if (errorCount > 3) {
                    run = false;
                    log.severe("There are too many errors in this connection. Stopping SocketListener");
                }
            }
        }
    }
