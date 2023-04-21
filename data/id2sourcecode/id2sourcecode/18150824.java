            @SuppressWarnings({ "unchecked" })
            public void deliver(Client fromClient, final Client toClient, Message msg) {
                Map<String, Object> data = (Map<String, Object>) msg.getData();
                Map<String, Object> message = new HashMap<String, Object>();
                message.put("test", "from server_user: " + data.get("test"));
                b.getChannel("/hello/world", false).publish(client, message, "new server message");
            }
