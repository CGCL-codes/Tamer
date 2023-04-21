    private void blockUntilIsConnected() throws IOException, SocketTimeoutException {
        while (!getChannel().finishConnect()) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException ignore) {
            }
        }
    }
