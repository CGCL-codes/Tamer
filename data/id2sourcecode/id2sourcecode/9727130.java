    public boolean isStarted(String name) throws ContainerException {
        Listener listener = null;
        Initiator initiator = null;
        Response response = null;
        boolean started = false;
        if (isCreated()) {
            if (listenerMap.containsKey(name)) {
                listener = listenerMap.get(name);
                if (listener.getChannel() != null && listener.getChannel().isOpen()) {
                    response = doIsBound(listener.getName());
                } else {
                    response = new Response();
                    response.setResult(Result.SUCCESS);
                }
            } else if (initiatorMap.containsKey(name)) {
                initiator = initiatorMap.get(name);
                if (initiator.getChannel() != null && initiator.getChannel().isOpen()) {
                    response = doIsConnected(initiator.getName());
                } else {
                    response = new Response();
                    response.setResult(Result.SUCCESS);
                }
            } else {
                throw new ContainerException("EXCEPTION: isStarted failed because name[" + name + "] not found.");
            }
            if (response.getResult() == Result.TRUE) {
                started = true;
            } else if (response.getResult() == Result.FALSE) {
                started = false;
            } else if (response.getResult() == Result.EXCEPTION) {
                throw new ContainerException(response.getException());
            }
        }
        return started;
    }
