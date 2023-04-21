    private synchronized void writeToClient(int action, Object... params) {
        if (clientOutputStream == null) {
            return;
        }
        try {
            clientOutputStream.writeInt(action);
            clientOutputStream.writeLong(Thread.currentThread().getId());
            for (Object param : params) {
                clientOutputStream.writeObject(param);
            }
            clientOutputStream.flush();
        } catch (IOException e) {
            closeClientSocket();
        }
    }
