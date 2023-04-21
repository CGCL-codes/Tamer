    @Override
    public void onNickChange(NickChangeEvent<KEllyBot> event) throws Exception {
        Collection<Channel> channels = event.getBot().getChannels();
        super.onNickChange(event);
        for (Channel c : channels) {
            if (c.getUsers().contains(event.getUser())) {
                updateWho(c);
                manageMessage(new Message(nc, event.getOldNick() + " is now known as " + event.getNewNick() + ".", c.getName(), c, Message.CONSOLE));
            }
        }
    }
