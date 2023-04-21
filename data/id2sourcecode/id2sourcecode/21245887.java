    private void close() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
            socket = null;
        }
        writerThread.close();
        readerThread.close();
    }
