    MessageImpl handleSubscribe(MessageImpl request) {
        String subscription = request.getSubscription();
        String id = request.getId();
        MessageImpl response = MessageImpl.newInstance(request.getChannel());
        response.setSubscription(subscription);
        response.setClientId(getId());
        response.setSuccessful(true);
        if (id != null) {
            response.setId(id);
        }
        bayeux.subscribe(subscription, this);
        return response;
    }
