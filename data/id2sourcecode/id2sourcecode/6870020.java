        public void handle(ClientImpl client, Transport transport, Map<String, Object> message) throws IOException {
            String channel_id = (String) message.get(CHANNEL_FIELD);
            if (client == null) {
                unknownClient(transport, channel_id);
                return;
            }
            String id = (String) message.get(ID_FIELD);
            ChannelId cid = getChannelId(channel_id);
            Object data = message.get("data");
            if (client == null) {
                if (_securityPolicy.authenticate((String) message.get("authScheme"), (String) message.get("authUser"), (String) message.get("authToken"))) client = newRemoteClient();
            }
            Map<String, Object> reply = new HashMap<String, Object>();
            reply.put(CHANNEL_FIELD, channel_id);
            if (id != null) reply.put(ID_FIELD, id);
            if (data != null && _securityPolicy.canSend(client, cid, message)) {
                publish(cid, client, data, id);
                reply.put(SUCCESSFUL_FIELD, Boolean.TRUE);
                reply.put("error", "");
            } else {
                reply.put(SUCCESSFUL_FIELD, Boolean.FALSE);
                reply.put("error", "unknown channel");
            }
            transport.send(reply);
        }
