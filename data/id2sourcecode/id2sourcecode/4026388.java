    public Record execute(InteractionSpec interactionSpec, Record inRecord) throws ResourceException {
        final Tracer tracer = baseTracer.entering("execute(InteractionSpec interactionSpec, Record inRecord)");
        Record outRecord = null;
        if (interactionSpec == null) {
            ResourceException re = new ResourceException("No interactionSpec provided.");
            tracer.throwing(re);
            throw re;
        }
        if (!(interactionSpec instanceof XIInteractionSpec)) {
            ResourceException re = new ResourceException("Provided interactionSpec is not valid.");
            tracer.throwing(re);
            throw re;
        }
        XIInteractionSpec xiInteractionSpec = (XIInteractionSpec) interactionSpec;
        String xiMethod = xiInteractionSpec.getFunctionName();
        if (xiMethod.compareTo(XIInteractionSpec.SEND) == 0) {
            Sender sender = SenderFactory.createSendSender();
            outRecord = sender.send(interactionSpec, inRecord, this.spiManagedConnection, this.spiManagedConnection.getChannelId());
        } else if (xiMethod.compareTo(XIInteractionSpec.CALL) == 0) {
            Sender sender = SenderFactory.createCallSender();
            outRecord = sender.send(interactionSpec, inRecord, this.spiManagedConnection, this.spiManagedConnection.getChannelId());
        } else {
            ResourceException re = new ResourceException("Unknown function name in interactionSpec: " + xiMethod);
            tracer.throwing(re);
            throw re;
        }
        tracer.leaving();
        return outRecord;
    }
