    public void invoke(DPWSContextImpl context) throws DPWSException {
        if (context.getBinding() != null) return;
        Channel c = context.getExchange().getInMessage().getChannel();
        ServicePort servicePort = (ServicePort) context.getProperty(DPWSContext.SERVICE_PORT);
        if (servicePort == null) {
            throw new DPWSFault("Could not service port.", DPWSFault.SENDER);
        }
        Binding binding = c.getTransport().findBinding(context, servicePort);
        if (binding == null) {
            throw new DPWSFault("Could not find an appropriate Transport Binding to invoke.", DPWSFault.SENDER);
        }
        context.setBinding(binding);
    }
