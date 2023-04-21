    public Object syncSend(Node node, Serializable message) {
        return readMessage(syncSend(node, writeMessage(message)));
    }
