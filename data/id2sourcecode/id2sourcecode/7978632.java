    public void invoke(DPWSContextImpl context) throws DPWSFault {
        try {
            Channel channel = context.getExchange().getOutMessage().getChannel();
            OutMessage message = context.getExchange().getOutMessage();
            channel.send(context, message);
        } catch (DPWSException e) {
            throw DPWSFault.createFault(e);
        }
    }
