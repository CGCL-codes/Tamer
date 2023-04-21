    MessageImpl handleDisconnect(MessageImpl request) {
        boolean isSuccessful = true;
        String requestId = request.getId();
        MessageImpl response = MessageImpl.newInstance(request.getChannel());
        response.setClientId(getId());
        response.setSuccessful(true);
        if (requestId != null) {
            response.setId(requestId);
        }
        close();
        return response;
    }
