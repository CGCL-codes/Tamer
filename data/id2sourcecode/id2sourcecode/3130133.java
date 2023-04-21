    void performSubscribe(final String toChannel, final IResultHandler resultHandler) throws IOException {
        final String reqId = Integer.toString(requestId++);
        MessageImpl msg = MessageImpl.newInstance(Bayeux.META_SUBSCRIBE);
        msg.setId(reqId);
        msg.setClientId(clientId);
        msg.setSubscription(toChannel);
        PostRequest request = newPostRequest(msg);
        BayeuxResponseHandler responseHandler = new BayeuxResponseHandler() {

            @Override
            public void onMessages(List<MessageImpl> messages) throws IOException {
                for (MessageImpl message : messages) {
                    if (message.getChannel().equals(Bayeux.META_SUBSCRIBE)) {
                        if (message.isSuccessful()) {
                            clientId = message.getClientId();
                            String sub = message.getSubscription();
                            if (!sub.equals(toChannel)) {
                                System.out.println("error");
                            }
                            String requestId = message.getId();
                            if (requestId != null) {
                                if (!reqId.equals(requestId)) {
                                    System.out.println("error occured");
                                }
                            }
                            if (LOG.isLoggable(Level.FINE)) {
                                LOG.fine("subscribe response (" + toChannel + ") received " + requestId);
                            }
                            resultHandler.onResult(true);
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
            LOG.fine("sending subscribe (" + toChannel + ") request " + reqId);
        }
        httpClient.send(request, responseHandler);
    }
