    @Override
    public void init() throws ServletException {
        final Bayeux b = (Bayeux) getServletContext().getAttribute(Bayeux.DOJOX_COMETD_BAYEUX);
        final Client client = b.newClient("server_user");
        final Channel c = b.getChannel("/hello/test", true);
        c.subscribe(client);
        client.addListener(new MessageListener() {

            @SuppressWarnings({ "unchecked" })
            public void deliver(Client fromClient, final Client toClient, Message msg) {
                Map<String, Object> data = (Map<String, Object>) msg.getData();
                Map<String, Object> message = new HashMap<String, Object>();
                message.put("test", "from server_user: " + data.get("test"));
                b.getChannel("/hello/world", false).publish(client, message, "new server message");
            }
        });
    }
