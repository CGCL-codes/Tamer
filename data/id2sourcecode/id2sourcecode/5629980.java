    private void processGetContext(final String[] part) {
        if (part.length < GET_CONTEXT_PARTS) {
            usage(GET_CONTEXT);
            return;
        }
        int separator;
        for (separator = 1; separator < part.length; separator++) {
            if (part[separator].equals("/")) {
                break;
            }
        }
        if (separator == part.length || separator <= 0) {
            usage(GET_CONTEXT);
            return;
        }
        String[] agents = new String[separator - 1];
        String[] contexts = new String[part.length - 1 - separator];
        for (int i = 0; i < agents.length; i++) {
            agents[i] = part[i + 1];
        }
        int offset = separator + 1;
        for (int i = 0; i < contexts.length; i++) {
            contexts[i] = part[i + offset];
        }
        String reply;
        try {
            reply = cp.getContext(agents, contexts);
        } catch (UnknownContextException e) {
            sendError(e.getMessage());
            return;
        } catch (AgentNotFoundException e) {
            sendError(e.getMessage());
            return;
        }
        send(reply);
    }
