    public void onMessage(StartGameMessage m, List<Message> out) {
        Iterator<Client> players = getChannel().getPlayers();
        List<String> fastClients = new ArrayList<String>();
        List<String> normalClients = new ArrayList<String>();
        while (players.hasNext()) {
            Client client = players.next();
            if (client != null && client.getUser().isPlayer()) {
                if (client.getProtocol() instanceof TetrifastProtocol) {
                    fastClients.add(client.getUser().getName());
                } else {
                    normalClients.add(client.getUser().getName());
                }
            }
        }
        if (!normalClients.isEmpty() && !fastClients.isEmpty()) {
            TextMessage warning = createWarningMessage(normalClients, fastClients);
            out.add(warning);
            out.add(m);
            GmsgMessage gmsg = new GmsgMessage();
            gmsg.setKey(warning.getKey());
            gmsg.setParams(warning.getParams());
            out.add(gmsg);
        } else {
            out.add(m);
        }
    }
