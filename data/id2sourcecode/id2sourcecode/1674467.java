    private String getReferenceId(WebContext context, String reference) {
        Channel channel = context.getChannel();
        WebServer server = context.getServer();
        int i = reference.indexOf('.');
        if (i >= 0) {
            String componentName = reference.substring(0, i);
            String methodName = reference.substring(i + 1);
            ComponentId componentId = channel.getComponentId(componentName);
            if (componentId != null) {
                MethodId methodId = componentId.getMethodId(methodName);
                if (methodId != null) return server.elementIdToId(methodId);
            }
        } else {
            ComponentId componentId = channel.getComponentId(reference);
            if (componentId != null) return server.elementIdToId(componentId);
        }
        return null;
    }
