    protected void processInOut(MessageExchange exchange, NormalizedMessage in, NormalizedMessage out) throws Exception {
        if (exchange.getStatus() == ExchangeStatus.ACTIVE) {
            out.setContent(in.getContent());
            getChannel().send(exchange);
        }
    }
