    protected void getChannelInformation(Message message) throws ModuleException {
        final Tracer tracer = baseTracer.entering("getChannelInformation(Message message, final Tracer tracer)");
        this.messageDirection = message.getMessageDirection().toString();
        if ((this.messageDirection == null) || (this.messageDirection.trim().equals(""))) {
            ErrorHelper.logErrorAndThrow(tracer, "Message direction could not be retrieved from message");
        }
        tracer.leaving();
    }
