    public void execute(CommandMessage m) {
        Client client = (Client) m.getSource();
        StringBuilder message = new StringBuilder();
        Iterator specators = client.getChannel().getSpectators();
        while (specators.hasNext()) {
            Client spectator = (Client) specators.next();
            message.append(spectator.getUser().getName());
            if (specators.hasNext()) {
                message.append(", ");
            }
        }
        PlineMessage response = new PlineMessage();
        response.setKey("command.speclist.format", message.toString());
        client.send(response);
    }
