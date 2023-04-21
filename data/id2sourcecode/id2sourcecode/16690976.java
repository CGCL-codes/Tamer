    public void execute(CommandMessage m) {
        Client client = (Client) m.getSource();
        User user = client.getUser();
        if ("true".equals(user.getProperty("command.ping"))) return;
        PlayerNumMessage response = new PlayerNumMessage();
        response.setSlot(client.getChannel().getClientSlot(client));
        user.setProperty("command.ping", "true");
        user.setProperty("command.ping.time", new Long(System.currentTimeMillis()));
        client.send(response);
    }
