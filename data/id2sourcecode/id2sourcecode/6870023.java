        public void handle(ClientImpl client, Transport transport, Map<String, Object> message) throws IOException {
            if (client == null) {
                unknownClient(transport, META_SUBSCRIBE);
                return;
            }
            _root.publish(META_SUBSCRIBE_ID, client, message);
            String subscribe_id = (String) message.get(SUBSCRIPTION_FIELD);
            if (subscribe_id == null) {
                subscribe_id = Long.toString(getRandom(message.hashCode() ^ client.hashCode()), 36);
                while (getChannel(subscribe_id) != null) subscribe_id = Long.toString(getRandom(message.hashCode() ^ client.hashCode()), 36);
            }
            ChannelId cid = getChannelId(subscribe_id);
            Map<String, Object> reply = new HashMap<String, Object>();
            reply.put(CHANNEL_FIELD, META_SUBSCRIBE);
            reply.put(SUBSCRIPTION_FIELD, subscribe_id);
            if (_securityPolicy.canSubscribe(client, cid, message)) {
                ChannelImpl channel = getChannel(cid);
                if (channel == null && _securityPolicy.canCreate(client, cid, message)) channel = (ChannelImpl) getChannel(subscribe_id, true);
                if (channel != null) {
                    channel.subscribe(client);
                    reply.put(SUCCESSFUL_FIELD, Boolean.TRUE);
                    reply.put("error", "");
                } else {
                    reply.put(SUCCESSFUL_FIELD, Boolean.FALSE);
                    reply.put("error", "cannot create");
                }
            } else {
                reply.put(SUCCESSFUL_FIELD, Boolean.FALSE);
                reply.put("error", "cannot subscribe");
            }
            transport.send(reply);
            _root.publish(META_SUBSCRIBE_ID, client, reply);
        }
