    private void breakOperationAndFlushReceiverBuffer() throws IOException {
        sendToPager(PING_CMD, 300);
        int c = 0;
        while (inputStream.available() > 0) {
            inputStream.read();
            c++;
        }
        SerialCardioTerminalImpl.writeToLog("Trashed buffer: read [" + c + "] bytes");
    }
