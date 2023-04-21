    public void receiveChatMessage(ChatMessage message) throws RemoteException {
        System.out.println(message.getChannel() + ": " + message.getMessage());
    }
