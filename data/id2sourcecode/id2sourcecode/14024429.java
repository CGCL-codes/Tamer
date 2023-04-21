    public void onReceive(DPWSContextImpl context, InMessage msg) {
        if (context.getExchange() == null) {
            MessageExchange exchange = new MessageExchange(context);
            exchange.setInMessage(msg);
            context.setCurrentMessage(msg);
        }
        HandlerPipeline pipeline = new HandlerPipeline(context.getDpws().getInPhases());
        pipeline.addHandlers(context.getDpws().getInHandlers());
        pipeline.addHandlers(msg.getChannel().getTransport().getInHandlers());
        if (context.getService() != null) {
            pipeline.addHandlers(context.getService().getInHandlers());
            context.setProperty(SERVICE_HANDLERS_REGISTERED, Boolean.TRUE);
        }
        context.setInPipeline(pipeline);
        if (context.getFaultHandler() == null) context.setFaultHandler(createFaultHandler());
        try {
            pipeline.invoke(context);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                log.error("onReceive", e);
            }
            context.setProperty(DefaultFaultHandler.EXCEPTION, e);
            try {
                context.getFaultHandler().invoke(context);
            } catch (Exception e1) {
                log.warn("Error invoking fault handler.", e1);
            }
        }
    }
