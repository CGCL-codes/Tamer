    public void stop(String name) throws ContainerException {
        Listener listener = null;
        Initiator initiator = null;
        Response response = null;
        if (listenerMap.containsKey(name)) {
            listener = listenerMap.get(name);
            if (listener.getChannel() != null && listener.getChannel().isOpen()) {
                response = doUnbind(listener.getName());
            } else {
                response = new Response();
                response.setResult(Result.SUCCESS);
            }
        } else if (initiatorMap.containsKey(name)) {
            initiator = initiatorMap.get(name);
            if (initiator.getChannel() != null && initiator.getChannel().isOpen()) {
                response = doDisconnect(initiator.getName());
            } else {
                response = new Response();
                response.setResult(Result.SUCCESS);
            }
        } else {
            throw new ContainerException("EXCEPTION: Stop failed because name[" + name + "] not found.");
        }
        if (response.getResult() == Result.EXCEPTION) {
            throw new ContainerException(response.getException());
        }
    }
