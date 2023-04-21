    protected Channel getChannelToUse(InternalExchange exchange) {
        Channel channelToUse = channel;
        if (exchange.getSource() == null) {
            try {
                String sender = (String) exchange.getProperty(SENDER_ENDPOINT);
                if (sender != null) {
                    int idx = sender.lastIndexOf(':');
                    String svc = sender.substring(0, idx);
                    String ep = sender.substring(idx + 1);
                    Map<String, Object> props = ServiceHelper.createMap(Endpoint.SERVICE_NAME, svc, Endpoint.ENDPOINT_NAME, ep);
                    List<Endpoint> eps = channel.getNMR().getEndpointRegistry().query(props);
                    if (eps != null && eps.size() == 1) {
                        channelToUse = ((InternalEndpoint) eps.get(0)).getChannel();
                    }
                }
            } catch (Throwable t) {
            }
        } else {
            channelToUse = exchange.getSource().getChannel();
        }
        return channelToUse;
    }
