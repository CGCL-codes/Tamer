    public void execute(CommandMessage m) {
        Client client = (Client) m.getSource();
        String emote = m.getText();
        PlineActMessage response = new PlineActMessage(emote);
        response.setSlot(client.getChannel().getClientSlot(client));
        response.setSource(client);
        client.getChannel().send(response);
    }
