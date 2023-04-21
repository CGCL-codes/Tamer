    public void process(final Exchange e) throws Exception {
        CamelMediationExchange exchange = (CamelMediationExchange) e;
        channelRef = ((AbstractCamelEndpoint) getEndpoint()).getChannelRef();
        endpointRef = ((AbstractCamelEndpoint) getEndpoint()).getEndpointRef();
        action = ((AbstractCamelEndpoint) getEndpoint()).getAction();
        isResponse = ((AbstractCamelEndpoint) getEndpoint()).isResponse();
        if (logger.isDebugEnabled()) logger.debug("Processing Message Exchange [" + exchange + "] Channel Reference [" + channelRef + "] " + "] Endpoint Reference [" + endpointRef + "] " + "] Channel Action [" + action + "] " + "] Is Response [" + isResponse + "] " + "] Producer [" + getClass().getSimpleName() + "] ");
        registry = exchange.getContext().getRegistry();
        applicationContext = registry.lookup("applicationContext", ApplicationContext.class);
        if (channelRef == null) throw new IdentityMediationException("Channel reference cannot be null, check your camel route definition!");
        channel = (Channel) applicationContext.getBean(channelRef);
        if (channel == null) throw new IdentityMediationException("Cannot resovle channel reference [" + channelRef + "], check your configuration!");
        if (endpointRef == null) throw new IdentityMediationException("Endpoint reference cannot be null, check your camel route definition!");
        for (IdentityMediationEndpoint endpoint : channel.getEndpoints()) {
            if (endpoint.getName().equals(endpointRef)) {
                this.endpoint = endpoint;
                break;
            }
        }
        if (this.endpoint == null) throw new IdentityMediationException("Cannot resolve endpoint reference [" + endpointRef + "] for channel [" + channelRef + "]");
        if (logger.isDebugEnabled()) logger.debug("Processing Message Exchange [" + exchange + "] Channel Object [" + channel + "] " + "] Endpoint Object [" + endpoint + "] ");
        try {
            if (isResponse) {
                doProcessResponse((E) exchange);
            } else {
                doProcess((E) exchange);
            }
            MediationLogger logger = channel.getIdentityMediator().getLogger();
            if (logger != null && channel.getIdentityMediator().isLogMessages()) {
                if (exchange.getFault(false) != null) logger.logFault(exchange.getFault(false));
                logger.logOutgoing(exchange.getOut());
            }
        } catch (IdentityMediationFault err) {
            String errorMsg = "[" + channel.getName() + "@" + channel.getLocation() + "] " + getClass().getSimpleName() + ":'" + err.getMessage() + "'";
            if (logger.isDebugEnabled()) logger.debug("Generating Fault message for " + errorMsg, err);
            CamelMediationMessage fault = (CamelMediationMessage) exchange.getFault();
            fault.setBody(new MediationMessageImpl(fault.getMessageId(), errorMsg, err));
        } catch (Exception err) {
            IdentityMediationFault f = new IdentityMediationFault("urn:org:atricore:idbus:error:fatal", null, null, err.getMessage(), err);
            if (logger.isDebugEnabled()) logger.debug("Generating Fault message for " + f.getMessage(), f);
            CamelMediationMessage fault = (CamelMediationMessage) exchange.getFault();
            fault.setBody(new MediationMessageImpl(fault.getMessageId(), f.getMessage(), f));
        }
    }
