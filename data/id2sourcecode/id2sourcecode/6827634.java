    MessageImpl handleConnect(MessageImpl request) {
        boolean isSuccessful = true;
        connectionType = BayeuxBroker.resolve(request.getConnectionType());
        String requestId = request.getId();
        if (!isConnected) {
            MessageImpl response = MessageImpl.newInstance(request.getChannel());
            response.setClientId(getId());
            response.setSuccessful(true);
            if (requestId != null) {
                response.setId(requestId);
            }
            isConnected = true;
            return response;
        } else {
            return null;
        }
    }
