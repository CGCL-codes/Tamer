    public SMAPRequestEnvelope(SMAPParsedEnvelope packet) {
        this.header = packet.getHeader();
        this.credentials = packet.getCredentials();
        this.messages = packet.getMessages();
        this.channel = packet.getChannel();
    }
