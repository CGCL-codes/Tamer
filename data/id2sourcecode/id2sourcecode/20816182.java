    public void invoke(final DPWSContextImpl context) throws DPWSException {
        try {
            OperationInfo aoi = context.getExchange().getOperation();
            final ServiceEndpoint service = context.getService();
            Runnable runnable = new ServiceRunner() {

                public void run() {
                    try {
                        sendMessage(context, service);
                    } catch (Exception e) {
                        context.setProperty(DefaultFaultHandler.EXCEPTION, e);
                        DPWSFault fault;
                        if (e instanceof DPWSFault) fault = (DPWSFault) e; else fault = DPWSFault.createFault(e);
                        try {
                            if (context.getOutPipeline() == null) {
                                HandlerPipeline pipeline = new HandlerPipeline(context.getDpws().getOutPhases());
                                pipeline.addHandlers(context.getService().getOutHandlers());
                                pipeline.addHandlers(context.getDpws().getOutHandlers());
                                OutMessage msg = context.getExchange().getOutMessage();
                                if (msg != null) pipeline.addHandlers(msg.getChannel().getTransport().getOutHandlers());
                                context.setOutPipeline(pipeline);
                            }
                            context.getOutPipeline().handleFault(fault, context);
                            context.getFaultHandler().invoke(context);
                        } catch (Exception e1) {
                            log.warn("Error invoking fault handler.", e1);
                        }
                    }
                }
            };
            ServiceEndpoint service2 = context.getService();
            execute(runnable, service2, aoi);
        } catch (Exception e) {
            log.warn("Error invoking service.", e);
            throw new DPWSFault("Error invoking service" + (e.getMessage() != null ? ": " + e.getMessage() : "."), e, DPWSFault.SENDER);
        }
    }
