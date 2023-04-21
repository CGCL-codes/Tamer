    @Override
    public void onQuit(QuitEvent<KEllyBot> event) throws Exception {
        for (Channel c : event.getUser().getChannels()) {
            if (c.getUsers().contains(event.getBot().getUserBot())) {
                updateWho(c);
                manageMessage(new Message(nc, event.getUser().getNick() + " has quit IRC. " + (event.getReason() != "" ? "(" + event.getReason() + ")" : ""), c.getName(), c, Message.CONSOLE));
            }
        }
        super.onQuit(event);
    }
