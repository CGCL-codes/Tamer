    @Override
    public void onDisconnect(DisconnectEvent<KEllyBot> event) throws Exception {
        super.onDisconnect(event);
        LinkedList<Room> rooms = event.getBot().getConnection().getRooms();
        for (Room r : rooms) {
            RoomManager.enQueue(new Message(event.getBot(), "Disconnected.", KEllyBot.systemName, r.getChannelName(), Message.CONSOLE));
        }
    }
