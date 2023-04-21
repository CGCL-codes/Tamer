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
