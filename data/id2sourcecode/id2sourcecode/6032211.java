    public SkytraqTransportMessageModel(byte[] payload) {
        this.messageID = payload[0];
        this.messageBody = new byte[payload.length - 1];
        for (int i = messageBody.length - 1; i >= 0; i--) {
            messageBody[i] = payload[i + 1];
        }
    }
