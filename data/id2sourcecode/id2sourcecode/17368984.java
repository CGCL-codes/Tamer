    public synchronized void serialSend(byte[] data) {
        connectedThread.write(data);
    }
