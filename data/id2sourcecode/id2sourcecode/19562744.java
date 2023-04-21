    @Override
    public void onJoin(JoinEvent<KEllyBot> event) throws Exception {
        if (botEqualsUser(event.getUser())) {
            nc.createRoom(event.getChannel().getName(), Room.IO | Room.TOPIC | Room.WHO, event.getChannel());
        }
        super.onJoin(event);
        queueMessage(new Message(nc, event.getUser().getNick() + " has joined.", event.getChannel().getName(), event.getChannel().getName(), Message.CONSOLE));
        updateWho(event.getChannel());
    }
