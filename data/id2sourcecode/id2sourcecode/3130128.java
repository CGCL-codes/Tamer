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
