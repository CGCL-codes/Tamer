    protected void getMessageDirection(Message message) throws ModuleException {
        final Tracer tracer = baseTracer.entering("getChannelInformation(Message message)");
        this.messageDirection = message.getMessageDirection().toString();
        checkValueForExistence(tracer, this.messageDirection, "messageDirection");
        tracer.leaving();
    }
