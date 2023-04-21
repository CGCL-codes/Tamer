    public void chat(UUID clientId, ChatMessage message) throws RemoteException {
        Set<UUID> keys = this.server.getClients().keySet();
        for (Iterator<UUID> i = keys.iterator(); i.hasNext(); ) {
            ClientService actClient = this.server.getClients().get(i.next());
            if (actClient.getChatChannel() == message.getChannel()) {
                actClient.receiveChatMessage(message);
            }
        }
    }
