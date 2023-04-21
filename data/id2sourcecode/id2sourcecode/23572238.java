    public void dispatch(InternalExchange exchange) {
        InternalEndpoint endpoint = exchange.getRole() == Role.Consumer ? exchange.getDestination() : exchange.getSource();
        endpoint.getChannel().deliver(exchange);
    }
