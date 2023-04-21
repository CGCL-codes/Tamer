    public Client getClientParameter(int i) {
        Client client = null;
        String param = getParameter(i);
        try {
            int slot = Integer.parseInt(param);
            if (slot >= 1 && slot <= 6) {
                if (getSource() instanceof Client) {
                    Channel channel = ((Client) getSource()).getChannel();
                    client = channel.getClient(slot);
                }
            }
        } catch (NumberFormatException e) {
        }
        if (client == null) {
            ClientRepository repository = ClientRepository.getInstance();
            client = repository.getClient(param);
        }
        return client;
    }
