    public void invoke(DPWSContextImpl context) throws DPWSFault {
        MessageExchange exchange = context.getExchange();
        AbstractMessage faultMessage = exchange.getFaultMessage();
        Channel faultChannel = faultMessage.getChannel();
        try {
            if (faultChannel != null) {
                OutMessage outMessage = (OutMessage) faultMessage;
                faultChannel.send(context, outMessage);
            } else System.out.println("... Current faultChannel is null ... reject fault Message!");
        } catch (DPWSException e) {
        }
    }
