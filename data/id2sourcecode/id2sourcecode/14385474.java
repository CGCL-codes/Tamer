    @Override
    protected void processInOut(MessageExchange exchange, NormalizedMessage in, NormalizedMessage out) throws Exception {
        if (exchange.getStatus() == ExchangeStatus.DONE) {
            return;
        } else if (exchange.getStatus() == ExchangeStatus.ERROR) {
            return;
        } else if (exchange.getFault() != null) {
            exchange.setStatus(ExchangeStatus.DONE);
            getChannel().send(exchange);
        } else {
            SubmitSM sm = marshaler.fromNMS(connection, exchange, in);
            SubmitSMResp smr = (SubmitSMResp) connection.sendRequest(sm);
            int code = smr.getCommandStatus();
            if (code == 0) {
                MessageUtil.transferInToOut(exchange, exchange);
                getChannel().send(exchange);
            } else {
                fail(exchange, new Exception("Unable to deliver message. Return-Code: " + code));
            }
        }
    }
