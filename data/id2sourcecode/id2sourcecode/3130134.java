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
