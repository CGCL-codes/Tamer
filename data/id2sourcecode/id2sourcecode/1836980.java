    public void tcpRun() {
        while (isRunning() && !tcpSocket.isClosed()) {
            try {
                Connection.start(this, tcpSocket.accept().getChannel());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
