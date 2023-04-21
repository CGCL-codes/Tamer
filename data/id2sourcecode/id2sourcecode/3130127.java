    void performHandshake(final IResultHandler resultHandler) throws IOException {
        final String reqId = Integer.toString(requestId++);
        MessageImpl msg = MessageImpl.newInstance(Bayeux.META_HANDSHAKE);
        msg.setId(reqId);
        msg.setVersion(VERSION_FORMAT.format(REQUESTED_BAYEUX_VERSION));
        msg.setMinimumVersion(VERSION_FORMAT.format(MINIMUM_BAYEUX_VERSION));
        msg.setSupportedConnectionTypes("long-polling");
        PostRequest request = newPostRequest(msg);
        BayeuxResponseHandler responseHandler = new BayeuxResponseHandler() {

            @Override
            public void onMessages(List<MessageImpl> messages) throws IOException {
                for (MessageImpl message : messages) {
                    if (message.getChannel().equals(Bayeux.META_HANDSHAKE)) {
                        if (message.isSuccessful()) {
                            double version = Double.parseDouble(message.getVersion());
                            clientId = message.getClientId();
                            serverSupportedConnectionypes = message.getSupportedConnectionTypes();
                            String requestId = message.getId();
                            if (requestId != null) {
                                if (!reqId.equals(requestId)) {
                                    System.out.println("error occured");
                                }
                            }
                            if (LOG.isLoggable(Level.FINE)) {
                                LOG.fine("handeshake response received " + requestId);
                            }
                            performConnect(httpClient, resultHandler);
                        } else {
                        }
                    } else {
                    }
                }
            }

            public void onException(IOException ioe) {
            }
        };
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("sending handeshake request " + reqId);
        }
        httpClient.send(request, responseHandler);
    }
