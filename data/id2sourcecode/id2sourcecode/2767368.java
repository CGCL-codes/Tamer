    public void onMessages(List<MessageImpl> messages, IHttpExchange exchange) {
        BayeuxClientProxy client = null;
        List<MessageImpl> responseMessages = new ArrayList<MessageImpl>();
        for (MessageImpl request : messages) {
            if (client == null) {
                synchronized (remoteClients) {
                    client = remoteClients.get(request.getClientId());
                }
            }
            if (client != null) {
                client.incMessageReceived();
            }
            if (request.isMetaMessage()) {
                if (request.getChannel().equals(Bayeux.META_HANDSHAKE)) {
                    if (client == null) {
                        client = new BayeuxClientProxy(this, "prx" + UUID.randomUUID().toString());
                        registerClient(client);
                    }
                    MessageImpl response = MessageImpl.newInstance(request.getChannel());
                    boolean successful = client.handleHandeshake(request, response);
                    if (successful) {
                        synchronized (remoteClients) {
                            remoteClients.put(client.getId(), client);
                        }
                    }
                    responseMessages.add(response);
                } else if (request.getChannel().equals(Bayeux.META_CONNECT)) {
                    if (client == null) {
                        System.out.println("Cleint is null");
                        throw new RuntimeException("client does not exists");
                    }
                    MessageImpl response = client.handleConnect(request);
                    if (response != null) {
                        responseMessages.add(response);
                    } else {
                        client.addExchange(exchange);
                    }
                } else if (request.getChannel().equals(Bayeux.META_DISCONNECT)) {
                    if (client == null) {
                        System.out.println("Cleint is null");
                    }
                    MessageImpl response = client.handleDisconnect(request);
                    if (response != null) {
                        responseMessages.add(response);
                    } else {
                        client.addExchange(exchange);
                    }
                } else if (request.getChannel().equals(Bayeux.META_SUBSCRIBE)) {
                    if (client == null) {
                        System.out.println("Cleint is null");
                    }
                    MessageImpl response = client.handleSubscribe(request);
                    responseMessages.add(response);
                } else if (request.getChannel().equals(Bayeux.META_UNSUBSCRIBE)) {
                    if (client == null) {
                        System.out.println("Cleint is null");
                    }
                    MessageImpl response = client.handleUnsubscribe(request);
                    responseMessages.add(response);
                } else {
                    System.out.println("UNKNOWN META MESSAGE");
                }
            } else {
                boolean isJsonCommentFiltered = false;
                if (client != null) {
                    if (client.isJsonCommentFiltered()) {
                        isJsonCommentFiltered = true;
                    }
                }
                String data = request.getData();
                String id = request.getId();
                String clientId = request.getClientId();
                publish(client, request.getChannel(), data, generatedMessageId());
                MessageImpl response = MessageImpl.newInstance(request.getChannel());
                response.setChannel(request.getChannel());
                response.setSuccessful(true);
                if (clientId != null) {
                    response.setClientId(clientId);
                }
                if (id != null) {
                    response.setId(id);
                }
                responseMessages.add(response);
            }
        }
        if (!responseMessages.isEmpty()) {
            boolean isJsonCommentFiltered = false;
            if (client != null) {
                isJsonCommentFiltered = client.isJsonCommentFiltered();
            }
            send(responseMessages, exchange, isJsonCommentFiltered);
        }
    }
