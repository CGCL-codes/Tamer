    public void send(MessageExchange exchange) throws MessagingException {
        assert exchange != null;
        createTarget(context.getNmr(), exchange);
        exchange.setProperty(SEND_SYNC, null);
        ((MessageExchangeImpl) exchange).afterSend();
        InternalExchange ie = (InternalExchange) ((MessageExchangeImpl) exchange).getInternalExchange();
        getChannelToUse(ie).send(ie);
    }
