    public SMAPParsedEnvelope parse(Object message) throws SMAPException {
        Message beepMessage;
        try {
            beepMessage = (Message) message;
        } catch (ClassCastException cce) {
            throw new SMAPException("received message not a beep message");
        }
        if (beepMessage == null) {
            throw new SMAPException("null message");
        }
        SMAPParsedEnvelope envelope = new SMAPParsedEnvelope();
        InputStream is = beepMessage.getDataStream().getInputStream();
        envelope.setCredentials(parseCredential(is, beepMessage.getChannel().getSession().getPeerCredential()));
        envelope.setHeader(parseHeader(is));
        parseMessages(is, envelope);
        return (envelope);
    }
