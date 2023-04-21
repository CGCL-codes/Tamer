    protected void sendRequest(List<Socket> sectionSockets) {
        for (Socket slaveSocket : sectionSockets) {
            try {
                slaveSocket.getChannel().write(_communicationBuffer);
                _communicationBuffer.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
