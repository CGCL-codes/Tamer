        public void handle(ClientImpl client, Transport transport, Map<String, Object> message) throws IOException {
            if (client == null) {
                unknownClient(transport, META_UNSUBSCRIBE);
                return;
            }
            _root.publish(META_UNSUBSCRIBE_ID, client, message);
            String channel_id = (String) message.get(SUBSCRIPTION_FIELD);
            ChannelImpl channel = getChannel(channel_id);
            if (channel != null) channel.unsubscribe(client);
            Map<String, Object> reply = new HashMap<String, Object>();
            reply.put(CHANNEL_FIELD, META_UNSUBSCRIBE);
            reply.put(SUBSCRIPTION_FIELD, channel.getId());
            reply.put(SUCCESSFUL_FIELD, Boolean.TRUE);
            reply.put("error", "");
            transport.send(reply);
            _root.publish(META_UNSUBSCRIBE_ID, client, reply);
        }
