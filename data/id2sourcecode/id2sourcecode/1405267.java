    public Record execute(InteractionSpec interactionSpec, Record inRecord) throws ResourceException {
        final Tracer tracer = baseTracer.entering("execute(InteractionSpec interactionSpec, Record inRecord)");
        Record outRecord = null;
        if (interactionSpec == null) {
            ResourceException re = new ResourceException("No interactionSpec provided.");
            tracer.throwing(re);
            throw re;
        }
        tracer.info("interactionSpec is not null.");
        if (!(interactionSpec instanceof XIInteractionSpec)) {
            ResourceException re = new ResourceException("Provided interactionSpec is not valid.");
            tracer.throwing(re);
            throw re;
        }
        tracer.info("interactionSpec is instance of XIInteractionSpec.");
        XIInteractionSpec xiInteractionSpec = (XIInteractionSpec) interactionSpec;
        String xiMethod = xiInteractionSpec.getFunctionName();
        tracer.info("xiMethod = " + xiMethod);
        if (xiMethod.compareTo(XIInteractionSpec.SEND) == 0) {
            Sender sender = SenderFactory.createSendSender();
            tracer.info("Created sendSender.");
            outRecord = sender.send(interactionSpec, inRecord, this.spiManagedConnection, this.spiManagedConnection.getChannelId());
            tracer.info("Sent sendSender.");
        } else if (xiMethod.compareTo(XIInteractionSpec.CALL) == 0) {
            Sender sender = SenderFactory.createCallSender();
            tracer.info("Created callSender.");
            outRecord = sender.send(interactionSpec, inRecord, this.spiManagedConnection, this.spiManagedConnection.getChannelId());
            tracer.info("Sent callSender.");
        } else {
            ResourceException re = new ResourceException("Unknown function name in interactionSpec: " + xiMethod);
            tracer.info("ResourceException created.");
            tracer.throwing(re);
            throw re;
        }
        tracer.leaving();
        return outRecord;
    }
