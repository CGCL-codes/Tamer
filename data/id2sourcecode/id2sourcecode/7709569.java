    public MediationMessage createMessage(CamelMediationMessage message) {
        Exchange exchange = message.getExchange().getExchange();
        logger.debug("Create Message Body from exchange " + exchange.getClass().getName());
        Message httpMsg = exchange.getIn();
        if (httpMsg.getHeader("http.requestMethod") == null) {
            throw new IllegalArgumentException("Unknown message, no valid HTTP Method header found!");
        }
        try {
            MediationState state = createMediationState(exchange);
            String jossoArtifact = state.getTransientVariable("josso_assertion_id");
            if (jossoArtifact == null) {
                throw new IllegalStateException("JOSSO Artifact (josso_assertion_id) not found in request");
            }
            String relayState = state.getTransientVariable("RelayState");
            JossoMediator mediator = (JossoMediator) getChannel().getIdentityMediator();
            MessageQueueManager aqm = mediator.getArtifactQueueManager();
            Object jossoMsg = aqm.pullMessage(new ArtifactImpl(jossoArtifact));
            return new MediationMessageImpl(httpMsg.getMessageId(), jossoMsg, null, relayState, null, state);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
