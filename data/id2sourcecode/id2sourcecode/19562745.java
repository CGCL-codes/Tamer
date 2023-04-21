    @Override
    public void onMode(ModeEvent<KEllyBot> event) throws Exception {
        super.onMode(event);
        updateWho(event.getChannel());
        queueMessage(new Message(nc, event.getMode() + " by " + event.getUser().getNick(), event.getChannel().getName(), event.getChannel().getName(), Message.CONSOLE));
    }
