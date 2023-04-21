            @Override
            public void onMessages(List<MessageImpl> messages) throws IOException {
                for (MessageImpl message : messages) {
                    if (message.getChannel().equals(Bayeux.META_CONNECT)) {
                        if (message.isSuccessful()) {
                            String cid = message.getClientId();
                            if (cid != null) {
                                if (!cid.equals(clientId)) {
                                    System.out.println("error occured");
                                }
                            }
                            String requestId = message.getId();
                            if (requestId != null) {
                                if (!reqId.equals(requestId)) {
                                    System.out.println("error occured");
                                }
                            }
                            if (LOG.isLoggable(Level.FINE)) {
                                LOG.fine("connect response received " + requestId);
                            }
                            resultHandler.onResult(true);
                        } else {
                        }
                    } else {
                    }
                }
            }
