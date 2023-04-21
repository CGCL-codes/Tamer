    protected void sendFault(DPWSFault fault, DPWSContextImpl context) {
        OutMessage outMsg = (OutMessage) context.getExchange().getFaultMessage();
        if (context.getService() != null) outMsg.setSerializer(context.getService().getFaultSerializer());
        outMsg.setBody(fault);
        context.setCurrentMessage(outMsg);
        DPWS dpws = context.getDpws();
        HandlerPipeline faultPipe = new HandlerPipeline(dpws.getFaultPhases());
        faultPipe.addHandlers(dpws.getFaultHandlers());
        Channel faultChannel = context.getExchange().getFaultMessage().getChannel();
        if (faultChannel != null) {
            faultPipe.addHandlers(faultChannel.getTransport().getFaultHandlers());
        }
        if (context.getService() != null) {
            faultPipe.addHandlers(context.getService().getFaultHandlers());
        }
        try {
            faultPipe.invoke(context);
        } catch (Exception e1) {
            e1.printStackTrace();
            DPWSFault fault2 = DPWSFault.createFault(e1);
            faultPipe.handleFault(fault2, context);
            System.out.println("DefaultFaultHandler.sendFault: Could not send fault.");
        }
    }
