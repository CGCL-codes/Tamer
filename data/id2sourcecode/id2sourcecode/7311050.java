    private Message handleSubscribeMessage(CommandMessage message) {
        GraniteContext context = GraniteContext.getCurrentInstance();
        Destination destination = context.getServicesConfig().findDestinationById(message.getMessageRefType(), message.getDestination());
        if (destination == null) return getInvalidDestinationError(message);
        GravityInvocationContext invocationContext = new GravityInvocationContext(message, destination);
        if (destination.getSecurizer() instanceof GravityDestinationSecurizer) {
            try {
                ((GravityDestinationSecurizer) destination.getSecurizer()).canSubscribe(invocationContext);
            } catch (Exception e) {
                return new ErrorMessage(message, e);
            }
        }
        GraniteConfig config = context.getGraniteConfig();
        if (config.hasSecurityService()) {
            try {
                config.getSecurityService().authorize(invocationContext);
            } catch (Exception e) {
                return new ErrorMessage(message, e);
            }
        }
        Channel channel = getChannel((String) message.getClientId());
        if (channel == null) return handleUnknownClientMessage(message);
        String subscriptionId = (String) message.getHeader(AsyncMessage.DESTINATION_CLIENT_ID_HEADER);
        if (subscriptionId == null) {
            subscriptionId = UUIDUtil.randomUUID();
            message.setHeader(AsyncMessage.DESTINATION_CLIENT_ID_HEADER, subscriptionId);
        }
        HttpSession session = null;
        if (context instanceof HttpGraniteContext) session = ((HttpGraniteContext) context).getSession(false);
        if (session != null && Boolean.TRUE.toString().equals(destination.getProperties().get("session-selector"))) {
            String selector = (String) session.getAttribute("org.granite.gravity.selector." + destination.getId());
            log.debug("Session selector found in session %s: %s", session.getId(), selector);
            if (selector != null) message.setHeader(CommandMessage.SELECTOR_HEADER, selector);
        }
        ServiceAdapter adapter = adapterFactory.getServiceAdapter(message);
        AsyncMessage reply = (AsyncMessage) adapter.manage(channel, message);
        postManage(channel);
        reply.setDestination(message.getDestination());
        reply.setClientId(channel.getId());
        reply.getHeaders().putAll(message.getHeaders());
        if (session != null && message.getDestination() != null) {
            session.setAttribute("org.granite.gravity.channel.clientId." + message.getDestination(), channel.getId());
            session.setAttribute("org.granite.gravity.channel.subscriptionId." + message.getDestination(), subscriptionId);
        }
        return reply;
    }
