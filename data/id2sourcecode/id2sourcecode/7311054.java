    private Message handlePublishMessage(AsyncMessage message, Channel channel) {
        GraniteContext context = GraniteContext.getCurrentInstance();
        Destination destination = context.getServicesConfig().findDestinationById(message.getClass().getName(), message.getDestination());
        if (destination == null) return getInvalidDestinationError(message);
        GravityInvocationContext invocationContext = new GravityInvocationContext(message, destination);
        if (destination.getSecurizer() instanceof GravityDestinationSecurizer) {
            try {
                ((GravityDestinationSecurizer) destination.getSecurizer()).canPublish(invocationContext);
            } catch (Exception e) {
                return new ErrorMessage(message, e, true);
            }
        }
        GraniteConfig config = context.getGraniteConfig();
        if (config.hasSecurityService() && context instanceof HttpGraniteContext) {
            try {
                config.getSecurityService().authorize(invocationContext);
            } catch (Exception e) {
                return new ErrorMessage(message, e, true);
            }
        }
        Channel fromChannel = channel;
        if (fromChannel == null) fromChannel = getChannel((String) message.getClientId());
        if (fromChannel == null) return handleUnknownClientMessage(message);
        ServiceAdapter adapter = adapterFactory.getServiceAdapter(message);
        AsyncMessage reply = (AsyncMessage) adapter.invoke(fromChannel, message);
        reply.setDestination(message.getDestination());
        reply.setClientId(fromChannel.getId());
        return reply;
    }
