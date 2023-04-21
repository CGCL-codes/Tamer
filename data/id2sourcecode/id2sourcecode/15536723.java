    private void blockUntilIsConnected() throws IOException, SocketTimeoutException {
        while (!getChannel().finishConnect()) {
            getChannel().configureBlocking(true);
            getChannel().finishConnect();
            getChannel().configureBlocking(false);
        }
    }
